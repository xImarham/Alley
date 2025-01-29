package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.arena.impl.StandAloneArena;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.runnable.MatchRunnable;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.hotbar.HotbarRepository;
import dev.revere.alley.hotbar.enums.HotbarType;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.cosmetic.impl.killeffects.KillEffectRepository;
import dev.revere.alley.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
    private Map<BlockState, Location> changedBlocks;
    private Map<BlockState, Location> brokenBlocks;
    private Map<BlockState, Location> placedBlocks;
    private final List<UUID> spectators;
    private List<Snapshot> snapshots;
    private MatchRunnable runnable;
    private EnumMatchState state;
    private final Queue queue;
    private final Arena arena;
    private boolean ranked;
    private long startTime;
    private final Kit kit;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param queue The queue of the match.
     * @param kit   The kit of the match.
     * @param arena The matchArena of the match.
     */
    public AbstractMatch(Queue queue, Kit kit, Arena arena, boolean ranked) {
        this.queue = queue;
        this.kit = kit;
        this.arena = arena;
        this.ranked = ranked;
        this.snapshots = new ArrayList<>();
        this.changedBlocks = new HashMap<>();
        this.placedBlocks = new HashMap<>();
        this.brokenBlocks = new HashMap<>();
        this.spectators = new CopyOnWriteArrayList<>();
        Alley.getInstance().getMatchRepository().getMatches().add(this);
    }

    /**
     * Starts the match by setting the state and updating player profiles.
     */
    public void startMatch() {
        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(true);
        }

        this.state = EnumMatchState.STARTING;
        this.runnable = new MatchRunnable(this);
        this.runnable.runTaskTimer(Alley.getInstance(), 0L, 20L);
        this.getParticipants().forEach(this::initializeParticipant);
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Initializes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to initialize.
     */
    private void initializeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            Player player = Alley.getInstance().getServer().getPlayer(gamePlayer.getUuid());
            if (player != null) {
                Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
                profile.setState(EnumProfileState.PLAYING);
                profile.setMatch(this);
                this.setupPlayer(player);
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
                player.getInventory().setArmorContents(getKit().getArmor());
                player.getInventory().setContents(getKit().getInventory());
            }
        }
    }

    /**
     * Ends the match.
     */
    public void endMatch() {
        this.placedBlocks.forEach((blockState, location) -> location.getBlock().setType(Material.AIR));
        this.placedBlocks.clear();

        this.brokenBlocks.forEach((blockState, location) -> location.getBlock().setType(blockState.getType()));
        this.brokenBlocks.clear();

        this.getParticipants().forEach(this::finalizeParticipant);
        this.spectators.forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                this.removeSpectator(player, false);
            }
        });

        Alley.getInstance().getMatchRepository().getMatches().remove(this);
        this.runnable.cancel();

        if (this.arena instanceof StandAloneArena) {
            ((StandAloneArena) this.arena).setActive(false);
        }
    }

    /**
     * Finalizes a game participant and updates the player profiles.
     *
     * @param gameParticipant The game participant to finalize.
     */
    private void finalizeParticipant(GameParticipant<MatchGamePlayerImpl> gameParticipant) {
        gameParticipant.getPlayers().forEach(gamePlayer -> {
            if (!gamePlayer.isDisconnected()) {
                Player player = Alley.getInstance().getServer().getPlayer(gamePlayer.getUuid());
                if (player != null) {
                    this.resetPlayerState(player);
                    Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
                    profile.setState(EnumProfileState.LOBBY);
                    profile.setMatch(null);
                    teleportPlayerToSpawn(player);
                }
            }
        });
    }

    /**
     * Teleports a player to the spawn and applies spawn items.
     *
     * @param player The player to teleport.
     */
    private void teleportPlayerToSpawn(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        Alley.getInstance().getSpawnService().teleportToSpawn(player);

        HotbarRepository hotbarRepository = Alley.getInstance().getHotbarRepository();
        if (profile.getParty() == null) {
            hotbarRepository.applyHotbarItems(player, HotbarType.LOBBY);
            return;
        }

        hotbarRepository.applyHotbarItems(player, HotbarType.PARTY);
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
     * Creates a snapshot of the match.
     *
     * @param loser  The loser of the match.
     * @param winner The winner of the match.
     */
    public void createSnapshot(UUID loser, UUID winner) {
        Snapshot winnerSnapshot = new Snapshot(Bukkit.getPlayer(winner), true);
        winnerSnapshot.setOpponent(loser);
        this.snapshots.add(winnerSnapshot);

        Snapshot loserSnapshot = new Snapshot(Bukkit.getPlayer(loser), false);
        loserSnapshot.setOpponent(winner);
        this.snapshots.add(loserSnapshot);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {
        if (!(this.state == EnumMatchState.STARTING || this.state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer.isDead()) {
            return;
        }

        this.setParticipantAsDead(player, gamePlayer);
        this.notifySpectators(player.getName() + " has died");
        //notifyParticipants(player.getName() + " has died");

        player.setVelocity(new Vector());

        this.handleRoundOrRespawn(player);

        if (this.getParticipants().size() == 2) {
            this.handleFinalMatchResult();
        }
    }

    /**
     * Handles the round or respawn of a player.
     *
     * @param player The player to handle the round or respawn of.
     */
    private void handleRoundOrRespawn(Player player) {
        if (this.canEndRound()) {
            this.state = EnumMatchState.ENDING_ROUND;
            this.handleRoundEnd();

            if (this.canEndMatch()) {
                Player killer = PlayerUtil.getLastAttacker(player);
                if (killer != null) {
                    this.handleEffects(player, killer);
                }
                this.state = EnumMatchState.ENDING_MATCH;
            }
            this.runnable.setStage(4);
        } else {
            this.handleRespawn(player);
        }
    }

    private void handleFinalMatchResult() {
        GameParticipant<MatchGamePlayerImpl> participantA = getParticipants().get(0);
        GameParticipant<MatchGamePlayerImpl> participantB = getParticipants().get(1);

        String winner;
        String loser;

        if (this.isParticipantDead(participantA) && !this.isParticipantDead(participantB)) {
            winner = participantB.getPlayers().get(0).getPlayer().getName();
            loser = participantA.getPlayers().get(0).getPlayer().getName();
        } else if (!this.isParticipantDead(participantA) && this.isParticipantDead(participantB)) {
            winner = participantA.getPlayers().get(0).getPlayer().getName();
            loser = participantB.getPlayers().get(0).getPlayer().getName();
        } else {
            winner = "error";
            loser = "error";
        }

        MatchUtility.sendMatchResult(this, winner, loser);
    }

    /**
     * Checks if a participant is dead.
     *
     * @param participant The participant to check.
     * @return True if the participant is dead.
     */
    private boolean isParticipantDead(GameParticipant<MatchGamePlayerImpl> participant) {
        return participant.getPlayers().stream().allMatch(MatchGamePlayerImpl::isDead);
    }

    /**
     * Sets a participant as dead.
     *
     * @param player     The player to set as dead.
     * @param gamePlayer The game player to set as dead.
     */
    private void setParticipantAsDead(Player player, MatchGamePlayerImpl gamePlayer) {
        if (this.kit.isSettingEnabled(KitSettingLivesImpl.class)) {
            if (this.getParticipant(player).getPlayer().getData().getLives() <= 0) {
                gamePlayer.setDead(true);
            } else {
                this.getParticipant(player).getPlayers().forEach(participant -> participant.setDead(false));
            }
        } else {
            gamePlayer.setDead(true);
        }
    }

    /**
     * Handles the effects of a player.
     *
     * @param player The player to handle the effects of.
     * @param killer The killer of the player.
     */
    private void handleEffects(Player player, Player killer) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(killer.getUniqueId());
        String selectedKillEffectName = profile.getProfileData().getProfileCosmeticData().getSelectedKillEffect();
        String selectedSoundEffectName = profile.getProfileData().getProfileCosmeticData().getSelectedSoundEffect();

        CosmeticRepository cosmeticRepository = Alley.getInstance().getCosmeticRepository();
        for (ICosmeticRepository<?> repository : cosmeticRepository.getCosmeticRepositories().values()) {
            if (repository instanceof KillEffectRepository) {
                KillEffectRepository killEffectRepository = (KillEffectRepository) repository;
                AbstractKillEffect killEffect = killEffectRepository.getByName(selectedKillEffectName);
                killEffect.spawnEffect(player);
            } else if (repository instanceof SoundEffectRepository) {
                SoundEffectRepository soundEffectRepository = (SoundEffectRepository) repository;
                AbstractSoundEffect soundEffect = soundEffectRepository.getByName(selectedSoundEffectName);
                soundEffect.spawnEffect(killer);
            }
        }
    }

    /**
     * Notifies the participants with a message.
     *
     * @param message The message to notify.
     */
    protected void notifyParticipants(String message) {
        this.getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
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
    protected void notifySpectators(String message) {
        if (this.spectators == null) {
            return;
        }
        this.spectators.stream()
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(CC.translate(message)));
    }

    public void handleLeaving(Player player) {
        if (!(this.state == EnumMatchState.STARTING || state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            if (!gamePlayer.isDead()) {
                if (this.kit.isSettingEnabled(KitSettingLivesImpl.class)) {
                    this.getParticipant(player).getPlayer().getData().setLives(0);
                    this.handleDeath(player);
                } else {
                    this.handleDeath(player);
                }
            }
        }
    }

    /**
     * Handles the disconnect of a player.
     *
     * @param player The player that disconnected.
     */
    public void handleDisconnect(Player player) {
        if (!(this.state == EnumMatchState.STARTING || this.state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            if (!gamePlayer.isDead()) {
                this.handleDeath(player);
            }
        }
    }

    /**
     * Handles the start of a round.
     */
    public void handleRoundStart() {
        this.snapshots.clear();
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        this.startTime = System.currentTimeMillis() - this.startTime;
        getParticipants().forEach(gameParticipant -> {
            if (gameParticipant.isAllDead()) {
                gameParticipant.getPlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    if (player != null && !gamePlayer.isDead()) {
                        Snapshot snapshot = new Snapshot(player, true);
                        this.snapshots.add(snapshot);
                    }
                });
            }
        });

        MatchRegularImpl match = (MatchRegularImpl) this;
        if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
            this.updateSnapshots(match);
        }
    }

    /**
     * Updates the snapshots of a match.
     *
     * @param match The match to update the snapshots of.
     */
    private void updateSnapshots(MatchRegularImpl match) {
        for (Snapshot snapshot : snapshots) {
            if (snapshot.getUuid().equals(match.getParticipantA().getPlayer().getUuid())) {
                snapshot.setOpponent(match.getParticipantB().getPlayer().getUuid());
            } else {
                snapshot.setOpponent(match.getParticipantA().getPlayer().getUuid());
            }
            Alley.getInstance().getSnapshotRepository().getSnapshots().put(snapshot.getUuid(), snapshot);
        }
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     */
    public void addSpectator(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPECTATING);
        profile.setMatch(this);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.SPECTATOR);

        if (arena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        player.teleport(arena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setAllowFlight(true);
        player.setFlying(true);

        spectators.add(player.getUniqueId());
        notifyParticipants(player.getName() + " is now spectating the match");
        notifySpectators(player.getName() + " is now spectating the match");
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove from spectating.
     */
    public void removeSpectator(Player player, boolean notify) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);

        player.setAllowFlight(false);
        player.setFlying(false);

        this.resetPlayerState(player);
        this.teleportPlayerToSpawn(player);
        this.spectators.remove(player.getUniqueId());

        if (notify) {
            this.notifyParticipants("&b" + player.getName() + " &ais no longer spectating the match");
            this.notifySpectators("&b" + player.getName() + " &ais no longer spectating the match");
        }
    }

    /**
     * Gets the duration of the match.
     *
     * @return The duration of the match.
     */
    public String getDuration() {
        if (this.state == EnumMatchState.STARTING) return EnumMatchState.STARTING.getDescription();
        if (this.state == EnumMatchState.ENDING_MATCH) return EnumMatchState.ENDING_MATCH.getDescription();
        else return getFormattedElapsedTime();
    }

    /**
     * Gets the elapsed time of the match.
     *
     * @return The elapsed time of the match.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }

    /**
     * Gets the formatted elapsed time of the match.
     *
     * @return The formatted elapsed time of the match.
     */
    public String getFormattedElapsedTime() {
        long elapsedSeconds = getElapsedTime() / 1000;
        return String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
    }

    /**
     * Gets the game player of a player.
     *
     * @param player The player to get the game player of.
     * @return The game player of the player.
     */
    public MatchGamePlayerImpl getGamePlayer(Player player) {
        return getParticipants().stream()
                .map(GameParticipant::getPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Sends a message to all participants.
     *
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        });
    }

    /**
     * Sends a spigot (clickable) message to all participants including spectators.
     *
     * @param message The message to send.
     */
    public void sendSpigotMessage(BaseComponent message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        });
    }

    /**
     * Sends a combined spigot (clickable) message to all participants including spectators.
     *
     * @param message The message to send.
     */
    public void sendCombinedSpigotMessage(BaseComponent... message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        }));

        getSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                player.spigot().sendMessage(message);
            }
        });
    }

    public GameParticipant<MatchGamePlayerImpl> getParticipant(Player player) {
        return getParticipants().stream()
                .filter(gameParticipant -> gameParticipant.containsPlayer(player.getUniqueId()))
                .findFirst()
                .orElse(null);
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

    /**
     * Adds a block to the changed blocks map with the intention to handle block changes.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToBrokenBlocksMap(BlockState blockState, Location location) {
        this.brokenBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the broken blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromBrokenBlocksMap(BlockState blockState, Location location) {
        this.brokenBlocks.remove(blockState, location);
    }

    /**
     * Handles the respawn of a player.
     *
     * @param player The player that respawned.
     */
    public abstract void handleRespawn(Player player);

    /**
     * Gets the participants of the match.
     *
     * @return The participants of the match.
     */
    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();

    /**
     * Checks if the round can start.
     *
     * @return True if the round can start.
     */
    public abstract boolean canStartRound();

    /**
     * Checks if the round can end.
     *
     * @return True if the round can end.
     */
    public abstract boolean canEndRound();

    /**
     * Checks if the match can end.
     *
     * @return True if the match can end.
     */
    public abstract boolean canEndMatch();
}