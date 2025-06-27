package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import dev.revere.alley.feature.layout.data.LayoutData;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.runnable.MatchRunnable;
import dev.revere.alley.game.match.runnable.other.MatchRespawnRunnable;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.tool.reflection.impl.TitleReflectionService;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private final boolean ranked;

    private final Map<BlockState, Location> brokenBlocks = new ConcurrentHashMap<>();
    private final Map<BlockState, Location> placedBlocks = new ConcurrentHashMap<>();
    private final List<UUID> spectators = new CopyOnWriteArrayList<>();

    private boolean teamMatch;
    private boolean affectStatistics = true;

    private MatchRunnable runnable;
    private EnumMatchState state;
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
        this.queue = Objects.requireNonNull(queue, "Queue cannot be null");
        this.kit = Objects.requireNonNull(kit, "Kit cannot be null");
        this.arena = Objects.requireNonNull(arena, "Arena cannot be null");
        this.ranked = ranked;

        this.plugin.getMatchService().getMatches().add(this);
    }

    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();

    public abstract void handleDisconnect(Player player);

    public abstract void handleRespawn(Player player);

    public abstract boolean canStartRound();

    public abstract boolean canEndRound();

    public abstract boolean canEndMatch();

    /**
     * Handles the item drop on death for a player.
     * This method clears the drops to prevent items from being dropped on death.
     *
     * @param player The player that died.
     * @param event  The PlayerDeathEvent that triggered this method.
     */
    public abstract void handleDeathItemDrop(Player player, PlayerDeathEvent event);

    /**
     * Starts the match by setting the state and updating player profiles and running the match runnable.
     */
    public void startMatch() {
        this.activateArenaIfStandalone();
        this.sendPlayerVersusPlayerMessage();

        this.state = EnumMatchState.STARTING;
        this.runnable = new MatchRunnable(this);
        this.runnable.runTaskTimer(this.plugin, 0L, 20L);
        this.getParticipants().forEach(this::initializeParticipant);
        this.startTime = System.currentTimeMillis();
    }

    public void endMatch() {
        // this.resetBlockChanges();
        deleteArenaCopyIfStandalone();

        this.getParticipants().forEach(this::finalizeParticipant);

        this.plugin.getMatchService().getMatches().remove(this);
        this.runnable.cancel();

        this.deactivateArenaIfStandalone();
    }

    private void activateArenaIfStandalone() {
        if (this.arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) this.arena;
            standAloneArena.setActive(true);
        }
    }

    private void deactivateArenaIfStandalone() {
        if (this.arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) this.arena;
            standAloneArena.setActive(false);
        }
    }

    private void deleteArenaCopyIfStandalone() {
        if (this.arena instanceof StandAloneArena) {
            StandAloneArena standAloneArena = (StandAloneArena) this.arena;
            standAloneArena.deleteCopiedArena();
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
                this.updatePlayerProfileForMatch(player);
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
        gameParticipant.getPlayers().stream()
                .filter(gamePlayer -> !gamePlayer.isDisconnected())
                .forEach(gamePlayer -> {
                    Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
                    if (player != null) {
                        finalizePlayer(player);
                    }
                });
    }

    /**
     * Method to finalize a player after the match ends.
     * This method resets the player's state, updates their profile for the lobby,
     *
     * @param player The player to finalize.
     */
    private void finalizePlayer(Player player) {
        this.resetPlayerState(player);
        updatePlayerProfileForLobby(player);
        this.plugin.getVisibilityService().updateVisibility(player);
        this.teleportPlayerToSpawn(player);
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
            PlayerUtil.reset(player, true);
            this.giveLoadout(player, this.kit);
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
        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (participant.isAllEliminated() && !gamePlayer.isDisconnected()) {
            return;
        }

        this.handleParticipant(player, gamePlayer);
        this.notifySpectators(player.getName() + " has died");

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        Player killer = this.plugin.getCombatService().getLastAttacker(player);
        this.handleDeathMessages(player, killer, profile);

        player.setVelocity(new Vector());

        if (this.canEndRound()) {
            this.state = EnumMatchState.ENDING_ROUND;
            this.handleRoundEnd();

            if (this.canEndMatch()) {
                if (killer != null) {
                    this.handleDeathEffects(player, killer);
                }

                this.state = EnumMatchState.ENDING_MATCH;
            }
            this.runnable.setStage(4);
        } else {
            if (!gamePlayer.isEliminated()) {
                if (this.shouldHandleRegularRespawn(player)) {
                    this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> this.handleRespawn(player), 1L);
                } else {
                    this.startRespawnProcess(player);
                }
            }
        }

        this.handleSpectator(player, profile, participant);
    }

    /**
     * Handles the player becoming a spectator based on the match state and kit settings.
     *
     * @param player      The player to handle.
     * @param profile     The profile of the player.
     * @param participant The participant of the match.
     */
    private void handleSpectator(Player player, Profile profile, GameParticipant<MatchGamePlayerImpl> participant) {
        Kit matchKit = profile.getMatch().getKit();

        MatchGamePlayerImpl gamePlayer = this.getFromAllGamePlayers(player);
        if (gamePlayer.isDisconnected()) {
            return;
        }

        if (this.shouldBecomeSpectatorForEliminationKit(participant, matchKit, player) || shouldBecomeSpectatorForNonRoundKit(participant, matchKit)) {
            profile.getMatch().addSpectator(player);
        }
    }

    private boolean shouldBecomeSpectatorForEliminationKit(GameParticipant<MatchGamePlayerImpl> participant, Kit matchKit, Player player) {
        if (!participant.isAllEliminated() && this.hasEliminationBasedKit(matchKit)) {
            MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
            return gamePlayer.isEliminated();
        }
        return false;
    }

    private boolean shouldBecomeSpectatorForNonRoundKit(GameParticipant<MatchGamePlayerImpl> participant, Kit matchKit) {
        return !participant.isAllDead() && !this.isRoundBasedKit(matchKit) && !this.hasEliminationBasedKit(matchKit);
    }

    /**
     * Checks if the kit is elimination-based.
     *
     * @param kit The kit to check.
     * @return True if the kit is elimination-based, false otherwise.
     */
    private boolean hasEliminationBasedKit(Kit kit) {
        return kit.isSettingEnabled(KitSettingBedImpl.class) || kit.isSettingEnabled(KitSettingCheckpointImpl.class) || kit.isSettingEnabled(KitSettingLivesImpl.class);
    }

    /**
     * Checks if the kit is round-based.
     *
     * @param kit The kit to check.
     * @return True if the kit is round-based, false otherwise.
     */
    private boolean isRoundBasedKit(Kit kit) {
        return kit.isSettingEnabled(KitSettingStickFightImpl.class) || kit.isSettingEnabled(KitSettingRoundsImpl.class);
    }

    /**
     * Handles the death messages for a player.
     *
     * @param player  The player that died.
     * @param killer  The player that killed the player.
     * @param profile The profile of the player that died.
     */
    private void handleDeathMessages(Player player, Player killer, Profile profile) {
        if (killer == null) {
            this.notifyParticipants("&c" + profile.getNameColor() + player.getName() + " &fdied.");
            return;
        }

        this.processKillerActions(player, killer, profile);
    }

    /**
     * Processes the actions of the killer when a player is killed.
     *
     * @param player  the player that died.
     * @param killer  the player that killed the victim.
     * @param profile the profile of the player that died.
     */
    private void processKillerActions(Player player, Player killer, Profile profile) {
        GameParticipant<MatchGamePlayerImpl> killerParticipant = getParticipant(killer);
        if (killerParticipant == null) {
            this.notifyParticipants("&c" + profile.getNameColor() + player.getName() + " &fwas slain by an unknown player.");
            return;
        }
        killerParticipant.getPlayer().getData().incrementKills();

        Profile killerProfile = this.plugin.getProfileService().getProfile(killer.getUniqueId());

        this.plugin.getReflectionRepository()
                .getReflectionService(ActionBarReflectionService.class)
                .sendDeathMessage(killer, player);

        this.notifyParticipants("&c" + profile.getNameColor() + player.getName() + " &fwas slain by &c" + killerProfile.getNameColor() + killer.getName() + "&f.");
    }

    /**
     * Handles applying all relevant on-kill cosmetic effects.
     * This is called when a player is confirmed to be eliminated from the match.
     *
     * @param player The player who died (the victim).
     * @param killer The player who got the kill.
     */
    private void handleDeathEffects(Player player, Player killer) {
        Profile profile = this.plugin.getProfileService().getProfile(killer.getUniqueId());

        String selectedKillEffectName = profile.getProfileData().getCosmeticData().getSelectedKillEffect();
        String selectedSoundEffectName = profile.getProfileData().getCosmeticData().getSelectedSoundEffect();

        this.applyCosmetic(EnumCosmeticType.KILL_EFFECT, selectedKillEffectName, player);
        this.applyCosmetic(EnumCosmeticType.SOUND_EFFECT, selectedSoundEffectName, killer);
    }

    /**
     * Applies a selected cosmetic to a target player in a generic, type-safe way.
     * This method is now updated to use the enum-based repository system.
     *
     * @param cosmeticType The type of cosmetic to apply.
     * @param cosmeticName The name of the cosmetic selected by the player.
     * @param targetPlayer The player to apply the effect to (e.g., the victim or the killer).
     */
    private void applyCosmetic(EnumCosmeticType cosmeticType, String cosmeticName, Player targetPlayer) {
        if (cosmeticName == null || cosmeticName.equalsIgnoreCase("None")) {
            return;
        }

        BaseCosmeticRepository<?> repository = this.plugin.getCosmeticRepository().getRepository(cosmeticType);
        if (repository == null) {
            Logger.logError("Could not find cosmetic repository for type " + cosmeticType.name());
            return;
        }

        AbstractCosmetic cosmetic = repository.getCosmetic(cosmeticName);
        if (cosmetic == null) {
            return;
        }

        cosmetic.execute(targetPlayer);
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

        this.handleSnapshots();
    }

    private void handleSnapshots() {
        this.getParticipants().forEach(gameParticipant -> {
            gameParticipant.getPlayers().forEach(gamePlayer -> {
                Player player = this.plugin.getServer().getPlayer(gamePlayer.getUuid());
                Snapshot snapshot = new Snapshot(player, gamePlayer.isDead());

                MatchGamePlayerData data = gamePlayer.getData();

                snapshot.setOpponent(this.getOpponent(player).getPlayer().getUuid());
                snapshot.setLongestCombo(data.getLongestCombo());
                snapshot.setTotalHits(data.getHits());
                snapshot.setThrownPotions(data.getThrownPotions());
                snapshot.setMissedPotions(data.getMissedPotions());

                //potions later

                this.plugin.getSnapshotRepository().addSnapshot(snapshot);
            });
        });
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     */
    public void addSpectator(Player player) {
        this.setupSpectatorProfile(player);

        this.plugin.getVisibilityService().updateVisibility(player);
        this.plugin.getHotbarService().applyHotbarItems(player);

        if (this.arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        ListenerUtil.teleportAndClearSpawn(player, this.arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        if (this.getParticipant(player) == null) {
            this.spectators.add(player.getUniqueId());
        }

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        this.notifyAll("&6" + profile.getNameColor() + player.getName() + " &fis now spectating the match.");
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
            this.notifyAll("&6" + profile.getNameColor() + player.getName() + " &fis no longer spectating the match.");
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

        MatchGamePlayerImpl gamePlayer = this.getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDead(false);
        }

        Location spawnLocation = this.arena.getCenter();
        ListenerUtil.teleportAndClearSpawn(player, spawnLocation);

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

        this.sendMessage("&6&lSpectators: &f" + String.join(", ", firstThreeSpectatorNames) +
                (remainingSpectators.isEmpty() ? "" : " &7(and &6" + remainingSpectators.size() + " &7more...)"));

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
     * Gets a game player from all game players in the match.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player, or null if not found.
     */
    public MatchGamePlayerImpl getFromAllGamePlayers(Player player) {
        return this.getParticipants().stream()
                .map(GameParticipant::getAllPlayers)
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
     * Gets the opposing participant in a two-sided match.
     *
     * @param player The player object of a player on one side.
     * @return The opposing GameParticipant, or null if it cannot be determined.
     */
    public GameParticipant<MatchGamePlayerImpl> getOpponent(Player player) {
        GameParticipant<MatchGamePlayerImpl> participant = this.getParticipant(player);
        if (participant == null) {
            return null;
        }

        return this.getParticipants().stream()
                .filter(p -> !p.equals(participant))
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

        double deltaX = Math.abs(playerLocation.getX() - location.getX());
        double deltaZ = Math.abs(playerLocation.getZ() - location.getZ());

        if (deltaX > 0.1 || deltaZ > 0.1) {
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


    @SuppressWarnings("deprecation")
    public void resetBlockChanges() {

        if (this.getKit().isSettingEnabled(KitSettingRaidingImpl.class)) {
            AbstractArena arena = this.getArena();
            Location pos1 = arena.getPos1();
            Location pos2 = arena.getPos2();

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
                            block.setType(Material.AIR);
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
        String prefix = CC.translate("&7[&6Match&7] &r");

        if (this.isTeamMatch()) {
            GameParticipant<MatchGamePlayerImpl> participantA = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> participantB = this.getParticipants().get(1);

            int teamSizeA = participantA.getPlayerSize();
            int teamSizeB = participantB.getPlayerSize();

            String message = CC.translate(prefix + "&6" + participantA.getPlayer().getUsername() + "'s Team &7(&a" + teamSizeA + "&7) &avs &6" + participantB.getPlayer().getUsername() + "'s Team &7(&a" + teamSizeB + "&7)");
            this.sendMessage(message);
        } else {
            GameParticipant<MatchGamePlayerImpl> participant = this.getParticipants().get(0);
            GameParticipant<MatchGamePlayerImpl> opponent = this.getParticipants().get(1);

            String message = CC.translate(prefix + "&6" + participant.getPlayer().getUsername() + " &avs &6" + opponent.getPlayer().getUsername());
            this.sendMessage(message);
        }
    }

    private void updatePlayerProfileForMatch(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.PLAYING);
        profile.setMatch(this);
    }

    private void updatePlayerProfileForLobby(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);
    }

    private void setupSpectatorProfile(Player player) {
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPECTATING);
        profile.setMatch(this);
        this.plugin.getVisibilityService().updateVisibility(player);
    }
}
