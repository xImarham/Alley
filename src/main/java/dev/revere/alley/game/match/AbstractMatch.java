package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.runnable.MatchRunnable;
import dev.revere.alley.game.match.runnable.other.MatchRespawnRunnable;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionService;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public abstract class AbstractMatch {
    protected final Alley plugin = Alley.getInstance();

    private final Queue queue;
    private final Kit kit;
    private final AbstractArena arena;

    private final List<UUID> spectators;

    private MatchRunnable runnable;
    private EnumMatchState state;

    private Map<BlockState, Location> brokenBlocks;
    private Map<BlockState, Location> placedBlocks;

    private boolean ranked;
    private boolean teamMatch;
    private boolean affectStatistics;

    private long startTime;
    private long endTime;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param queue  The queue associated with the match.
     * @param kit    The kit used in the match.
     * @param arena  The arena where the match takes place.
     * @param ranked Whether the match is ranked.
     */
    public AbstractMatch(Queue queue, Kit kit, AbstractArena arena, boolean ranked) {
        this.queue = queue;
        this.kit = kit;
        this.arena = arena;
        this.ranked = ranked;
        this.teamMatch = false;
        this.affectStatistics = true;
        this.placedBlocks = new HashMap<>();
        this.brokenBlocks = new HashMap<>();
        this.spectators = new CopyOnWriteArrayList<>();
        this.plugin.getMatchService().getMatches().add(this);
    }

    public abstract void handleRespawn(Player player);

    public abstract void handleDisconnect(Player player);

    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();

    public abstract boolean canStartRound();

    public abstract boolean canEndRound();

    public abstract boolean canEndMatch();

    /**
     * Starts the match by setting the state and updating player profiles and running the match runnable.
     */
    public void startMatch() {
        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(true);
        }

        this.sendPlayerVersusPlayerMessage();

        this.state = EnumMatchState.STARTING;
        this.runnable = new MatchRunnable(this);
        this.runnable.runTaskTimer(this.plugin, 0L, 20L);
        this.getParticipants().forEach(this::initializeParticipant);
        this.startTime = System.currentTimeMillis();
    }

    public void endMatch() {
        this.resetBlockChanges();

        this.getParticipants().forEach(this::finalizeParticipant);

        this.plugin.getMatchService().getMatches().remove(this);
        this.runnable.cancel();

        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(false);
        }
    }

    /**
     * Initializes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to initialize.
     */
    private void initializeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
            if (player != null) {
                Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
                profile.setState(EnumProfileState.PLAYING);
                profile.setMatch(this);

                this.plugin.getVisibilityService().updateVisibility(player);

                this.setupPlayer(player);
            }
        });
    }

    /**
     * Finalizes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to finalize.
     */
    private void finalizeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            if (!gamePlayer.isDisconnected()) {
                Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
                if (player != null) {
                    this.resetPlayerState(player);
                    Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
                    profile.setState(EnumProfileState.LOBBY);
                    profile.setMatch(null);

                    this.plugin.getVisibilityService().updateVisibility(player);

                    this.teleportPlayerToSpawn(player);
                }
            }
        });
    }

    /**
     * Sets up a player for the match.
     *
     * @param player The player to set up.
     */
    public void setupPlayer(Player player) {
        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDead(false);
            if (!gamePlayer.isDisconnected()) {
                PlayerUtil.reset(player, true);
                this.giveLoadout(player, this.kit);
            }
        }
    }

    /**
     * Gives a loadout to a player.
     *
     * @param player The player to give the kit to.
     */
    public void giveLoadout(Player player, Kit kit) {
        player.getInventory().setArmorContents(kit.getArmor());

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getProfileData().getLayoutData().getLayouts().size() > 1) {
            LayoutData kitLayout = profile.getProfileData().getLayoutData().getLayouts().get(kit.getName()).get(0);
            player.getInventory().setContents(kitLayout.getItems());
        } else {
            this.plugin.getLayoutService().giveBooks(player, kit.getName());
        }

        player.updateInventory();

        this.kit.applyPotionEffects(player);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {
        if (!(this.state == EnumMatchState.STARTING || this.state == EnumMatchState.RUNNING)) {
            return;
        }

        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (participant.isAllEliminated()) {
            Bukkit.broadcastMessage(player.getName() + " has died, but the participant is already eliminated.");
            return;
        }

        this.handleParticipant(player, gamePlayer);
        this.notifySpectators(player.getName() + " has died");

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        Player killer = this.plugin.getCombatService().getLastAttacker(player);
        handleDeathMessages(player, killer, profile);

        player.setVelocity(new Vector());

        if (this.canEndRound()) {
            this.state = EnumMatchState.ENDING_ROUND;
            this.handleRoundEnd();

            if (this.canEndMatch()) {
                if (killer != null) {
                    this.handleEffects(player, killer);
                }

                this.state = EnumMatchState.ENDING_MATCH;
            }
            this.runnable.setStage(4);
        } else {
            if (this.shouldHandleRegularRespawn(player)) {
                // for regular matches (not countdown respawn)
                this.handleRespawn(player);
            }
        }

        handleSpectator(player, profile, participant);
    }

    private void handleSpectator(Player player, Profile profile, GameParticipant<MatchGamePlayerImpl> participant) {
        if (!profile.getMatch().getParticipant(player).isAllEliminated() && profile.getMatch().getKit().isSettingEnabled(KitSettingBedImpl.class)) {
            Bukkit.broadcastMessage(player.getName() + " is dead, but the participant is not all eliminated.");
            MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);

            if (participant.isBedBroken() && gamePlayer.isEliminated()) {
                Bukkit.broadcastMessage(player.getName() + " is eliminated, no bed and participant is eliminated. Adding to spectators.");
                profile.getMatch().addSpectator(player);
                return;
            }
            return;
        }

        if (!profile.getMatch().getParticipant(player).isAllDead()) {
            Kit matchKit = profile.getMatch().getKit();
            if (matchKit.isSettingEnabled(KitSettingStickFightImpl.class) || matchKit.isSettingEnabled(KitSettingRoundsImpl.class)) {
                return;
            }

            MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
            Bukkit.broadcastMessage(player.getName() + " is dead, lives: " + gamePlayer.getData().getLives() + " user: " + gamePlayer.getUsername());
            if (matchKit.isSettingEnabled(KitSettingLivesImpl.class)) {
                if (gamePlayer.isEliminated()) {
                    profile.getMatch().addSpectator(player);
                    return;
                }
                return;
            }

            profile.getMatch().addSpectator(player);
        }
    }

    private void handleDeathMessages(Player player, Player killer, Profile profile) {
        if (killer == null) {
            notifyParticipants("&c" + profile.getNameColor() + player.getName() + " &fdied.");
            return;
        }
        GameParticipant<MatchGamePlayerImpl> killerParticipant = profile.getMatch().getParticipant(killer);
        killerParticipant.getPlayer().getData().incrementKills();

        Profile killerProfile = this.plugin.getProfileService().getProfile(killer.getUniqueId());

        this.plugin.getReflectionRepository().getReflectionService(ActionBarReflectionService.class).sendDeathMessage(killer, player);
        notifyParticipants("&c" + profile.getNameColor() + player.getName() + " &fwas slain by &c" + killerProfile.getNameColor() + killer.getName() + "&f.");
    }

    /**
     * Handles the effects of a player.
     *
     * @param player The player to handle the effects of.
     * @param killer The killer of the player.
     */
    private void handleEffects(Player player, Player killer) {
        Profile profile = this.plugin.getProfileService().getProfile(killer.getUniqueId());
        String selectedKillEffectName = profile.getProfileData().getCosmeticData().getSelectedKillEffect();
        String selectedSoundEffectName = profile.getProfileData().getCosmeticData().getSelectedSoundEffect();

        KillEffectRepository killEffectRepository = this.plugin.getCosmeticRepository().getCosmeticRepository(KillEffectRepository.class);
        SoundEffectRepository soundEffectRepository = this.plugin.getCosmeticRepository().getCosmeticRepository(SoundEffectRepository.class);

        AbstractKillEffect killEffect = killEffectRepository.getCosmetic(selectedKillEffectName);
        if (killEffect != null) {
            killEffect.spawnEffect(player);
        }

        AbstractSoundEffect soundEffect = soundEffectRepository.getCosmetic(selectedSoundEffectName);
        if (soundEffect != null) {
            soundEffect.spawnEffect(killer);
        }
    }

    /**
     * Handles the start of a round.
     */
    public void handleRoundStart() {
        if (this instanceof MatchRoundsImpl && ((MatchRoundsImpl) this).getCurrentRound() > 0) {
            return;
        }

        this.startTime = System.currentTimeMillis();
    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        this.endTime = System.currentTimeMillis();
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     */
    public void addSpectator(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPECTATING);
        profile.setMatch(this);

        this.plugin.getVisibilityService().updateVisibility(player);

        this.plugin.getHotbarService().applyHotbarItems(player, EnumHotbarType.SPECTATOR);

        if (this.arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        player.teleport(this.arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        // if they are a participant, dont add them to spectators- otherwise add them
        if (this.getParticipant(player) == null) {
            this.spectators.add(player.getUniqueId());
        }

        this.notifyAll("&b" + profile.getNameColor() + player.getName() + " &fis now spectating the match.");
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);

        Bukkit.broadcastMessage(player.getName() + "'s dead status: " + participant.getPlayers().stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .map(MatchGamePlayerImpl::isDead)
                .orElse(false));

        Bukkit.broadcastMessage(player.getName() + "'s eliminated status: " + participant.getPlayers().stream()
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .map(MatchGamePlayerImpl::isEliminated)
                .orElse(false));
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove from spectating.
     */
    public void removeSpectator(Player player, boolean notify) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);

        this.plugin.getVisibilityService().updateVisibility(player);

        player.setAllowFlight(false);
        player.setFlying(false);

        this.resetPlayerState(player);
        this.teleportPlayerToSpawn(player);
        this.spectators.remove(player.getUniqueId());

        if (notify) {
            this.notifyAll("&b" + profile.getNameColor() + player.getName() + " &fis no longer spectating the match.");
        }
    }

    /**
     * Starts the respawn process for a participant.
     *
     * @param player The player to start the respawn process for.
     */
    public void startRespawnProcess(Player player) {
        player.setGameMode(GameMode.SPECTATOR);
        player.setAllowFlight(true);
        player.setFlying(true);

        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        participant.getPlayers().forEach(gamePlayer -> {
            gamePlayer.setDead(false); // set player alive (allows other players to rape)
        });

        Location spawnLocation = participant.getPlayers().get(0).getUuid().equals(player.getUniqueId())
                ? this.arena.getPos1()
                : this.arena.getPos2();
        player.teleport(spawnLocation);

        new MatchRespawnRunnable(player, this, 3).runTaskTimer(this.plugin, 0L, 20L);
    }

    /**
     * Determines whether handleRespawn should be called for a player.
     * This method can be overridden by subclasses to control the respawn process.
     *
     * @param player The player to check.
     * @return True if handleRespawn should be called, false otherwise.
     */
    protected boolean shouldHandleRegularRespawn(Player player) {
        return true;
    }

    /**
     * Sets a participant as dead.
     *
     * @param player     The player to set as dead.
     * @param gamePlayer The game player to set as dead.
     */
    public void handleParticipant(Player player, MatchGamePlayerImpl gamePlayer) {
        gamePlayer.setDead(true);
    }

    /**
     * Notifies the participants with a message.
     *
     * @param message The message to notify.
     */
    public void notifyParticipants(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }));
    }

    /**
     * Notifies the spectators with a message.
     *
     * @param message The message to notify.
     */
    public void notifySpectators(String message) {
        if (this.spectators == null) return;
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(CC.translate(message)));
    }

    /**
     * Notifies all participants and spectators with a message.
     *
     * @param message The message to notify.
     */
    public void notifyAll(String message) {
        this.notifyParticipants(message);
        this.notifySpectators(message);
    }

    public void broadcastAndStopSpectating() {
        List<String> firstThreeSpectatorNames = new ArrayList<>();
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .limit(3)
                .forEach(player -> firstThreeSpectatorNames.add(player.getName()));

        List<Integer> remainingSpectators = new ArrayList<>();
        this.spectators.stream()
                .map(uuid -> this.plugin.getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .skip(3)
                .forEach(player -> remainingSpectators.add(player.getEntityId()));

        this.sendMessage("&b&lSpectators: &f" + String.join(", ", firstThreeSpectatorNames) +
                (remainingSpectators.isEmpty() ? "" : " &7(and &b" + remainingSpectators.size() + " &7more...)"));

        this.spectators.forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                this.removeSpectator(player, false);
            }
        });
    }

    /**
     * Gets the duration of the match.
     *
     * @return The duration of the match.
     */
    public String getDuration() {
        if (this.state == EnumMatchState.STARTING) {
            return this.getFormattedElapsedTime(this.getElapsedTime());
        } else if (this.state == EnumMatchState.ENDING_MATCH) {
            return this.getFormattedElapsedTime(this.endTime - this.startTime);
        } else {
            return this.getFormattedElapsedTime(this.getElapsedTime());
        }
    }

    /**
     * Gets the elapsed time of the match.
     *
     * @return The elapsed time of the match.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    /**
     * Gets the formatted elapsed time of the match.
     *
     * @return The formatted elapsed time of the match.
     */
    public String getFormattedElapsedTime(long elapsedMillis) {
        long elapsedSeconds = elapsedMillis / 1000;
        return String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
    }

    /**
     * Gets the game player of a player.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player.
     */
    public MatchGamePlayerImpl getGamePlayer(Player player) {
        return this.getParticipants().stream()
                .map(GameParticipant::getPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets a participant by a player.
     *
     * @param player The player to get the participant of.
     * @return The participant of the player.
     */
    public GameParticipant<MatchGamePlayerImpl> getParticipant(Player player) {
        return this.getParticipants().stream()
                .filter(gameParticipant -> gameParticipant.containsPlayer(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }


    /**
     * Plays a sound for a player.
     *
     * @param sound The sound to play.
     */
    public void playSound(Sound sound) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                SoundUtil.playCustomSound(player, sound, 1.0F, 1.0F);
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                SoundUtil.playCustomSound(player, sound, 1.0F, 1.0F);
            }
        });
    }

    /**
     * Sends a title to all participants and spectators.
     *
     * @param title    The title to send.
     * @param subtitle The subtitle to send.
     * @param fadeIn   The fade-in time in ticks.
     * @param stay     The stay time in ticks.
     * @param fadeOut  The fade-out time in ticks.
     */
    public void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                this.plugin.getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
                        player,
                        title,
                        subtitle,
                        fadeIn, stay, fadeOut
                );
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                this.plugin.getReflectionRepository().getReflectionService(TitleReflectionService.class).sendTitle(
                        player,
                        title,
                        subtitle,
                        fadeIn, stay, fadeOut
                );
            }
        });
    }

    /**
     * Sends a list of messages to all participants.
     *
     * @param messages The list of messages to send.
     */
    public void sendMessage(List<String> messages) {
        messages.forEach(this::sendMessage);
    }

    /**
     * Sends a message to all participants.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        }));

        this.getSpectators().forEach(uuid -> {
            Player player = this.plugin.getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(CC.translate(message));
            }
        });
    }

    /**
     * Checks if the attacker is in the same participant team as the supposed victim.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     * @return If the attacker is in the same participant team as the victim.
     */
    public boolean isInSameTeam(Player attacker, Player victim) {
        GameParticipant<MatchGamePlayerImpl> attackerParticipant = this.getParticipant(attacker);
        GameParticipant<MatchGamePlayerImpl> victimParticipant = this.getParticipant(victim);

        return attackerParticipant.equals(victimParticipant);
    }

    /**
     * Intentionally made to deny player movement during a match countdown.
     *
     * @param participants the participants
     */
    public void denyPlayerMovement(List<GameParticipant<MatchGamePlayerImpl>> participants) {
        if (participants.size() == 2) {
            GameParticipant<?> participantA = participants.get(0);
            GameParticipant<?> participantB = participants.get(1);

            Location locationA = this.arena.getPos1();
            Location locationB = this.arena.getPos2();

            for (GamePlayer gamePlayer : participantA.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationA);
                }
            }

            for (GamePlayer gamePlayer : participantB.getPlayers()) {
                Player participantPlayer = gamePlayer.getPlayer();
                if (participantPlayer != null) {
                    this.teleportBackIfMoved(participantPlayer, locationB);
                }
            }
        }
    }

    /**
     * Teleports the player back to their designated position if they moved.
     *
     * @param player   The player to check.
     * @param location The designated location.
     */
    private void teleportBackIfMoved(Player player, Location location) {
        Location playerLocation = player.getLocation();

        if (playerLocation.getBlockX() != location.getBlockX() || playerLocation.getBlockZ() != location.getBlockZ()) {
            player.teleport(new Location(location.getWorld(), location.getX(), playerLocation.getY(), location.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
        }
    }

    /**
     * Teleports a player to the spawn and applies spawn items.
     *
     * @param player The player to teleport.
     */
    private void teleportPlayerToSpawn(Player player) {
        if (player == null) return;
        this.plugin.getSpawnService().teleportToSpawn(player);
        this.plugin.getHotbarService().applyHotbarItems(player);
    }

    /**
     * Teleports a player to the spawn.
     *
     * @param player The player to teleport.
     */
    private void resetPlayerState(Player player) {
        player.setFireTicks(0);
        player.updateInventory();
        PlayerUtil.reset(player, false);
    }

    /**
     * Adds a block to the placed blocks map with the intention to handle block placement and removal.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the placed blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromPlacedBlocksMap(BlockState blockState, Location location) {
        this.placedBlocks.remove(blockState, location);
    }

    public void addBlockToBrokenBlocksMap(BlockState blockState, Location location) {
        if (this.placedBlocks.containsValue(location)) {
            this.placedBlocks.values().remove(location);
        } else {
            this.brokenBlocks.put(blockState, location);
        }
    }

    /**
     * Removes a block from the broken blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromBrokenBlocksMap(BlockState blockState, Location location) {
        this.brokenBlocks.remove(blockState, location);
    }

    @SuppressWarnings("deprecation")
    public void resetBlockChanges() {

        if (this.getKit().isSettingEnabled(KitSettingRaidingImpl.class)) {
            AbstractArena arena = this.getArena();
            Location pos1 = arena.getPos1();
            Location pos2 = arena.getPos2();

            // TODO: close all doors and gates in arena region if kit setting baseraiding is enabled

            for (int x = pos1.getBlockX(); x <= pos2.getBlockX(); x++) {
                for (int z = pos1.getBlockZ(); z <= pos2.getBlockZ(); z++) {
                    for (int y = pos1.getBlockY(); y <= pos2.getBlockY(); y++) {
                        Location location = new Location(pos1.getWorld(), x, y, z);
                        Block block = location.getBlock();
                        if (ListenerUtil.isDoorOrGate(block.getType())) {
                            BlockState originalState = block.getState();
                            if (originalState.getType() == Material.AIR) {
                                continue;
                            }
                            this.brokenBlocks.put(originalState, location);
                            block.setType(Material.AIR); // Remove the door or gate
                        }
                    }
                }
            }
        }


        this.removePlacedBlocks();

        for (Map.Entry<BlockState, Location> entry : this.brokenBlocks.entrySet()) {
            Location location = entry.getValue();
            BlockState originalState = entry.getKey();

            Block block = location.getBlock();
            block.setType(originalState.getType());
            block.setData(originalState.getRawData());
        }

        this.brokenBlocks.clear();
    }

    public void removePlacedBlocks() {
        for (Map.Entry<BlockState, Location> entry : this.placedBlocks.entrySet()) {
            Location location = entry.getValue();
            location.getBlock().setType(Material.AIR);
        }

        this.placedBlocks.clear();
    }


    private void sendPlayerVersusPlayerMessage() {
        String prefix = CC.translate("&7[&bMatch&7] &r");

        if (this.isTeamMatch()) {
            GameParticipant<MatchGamePlayerImpl> participantA = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> participantB = this.getParticipants().get(1);

            int teamSizeA = participantA.getPlayerSize();
            int teamSizeB = participantB.getPlayerSize();

            String message = CC.translate(prefix + "&b" + participantA.getPlayer().getUsername() + "'s Team &7(&a" + teamSizeA + "&7) &avs &b" + participantB.getPlayer().getUsername() + "'s Team &7(&a" + teamSizeB + "&7)");
            this.sendMessage(message);
        } else {
            GameParticipant<MatchGamePlayerImpl> participant = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> opponent = this.getParticipants().get(1);

            String message = CC.translate(prefix + "&b" + participant.getPlayer().getUsername() + " &avs &b" + opponent.getPlayer().getUsername());
            this.sendMessage(message);
        }
    }
}
