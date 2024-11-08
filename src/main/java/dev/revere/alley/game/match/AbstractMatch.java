package dev.revere.alley.game.match;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.arena.impl.StandAloneArena;
import dev.revere.alley.config.ConfigHandler;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRegularImpl;
import dev.revere.alley.game.match.player.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.runnable.MatchRunnable;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.hotbar.enums.HotbarType;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.profile.cosmetic.impl.killeffects.KillEffectRepository;
import dev.revere.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.profile.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.profile.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.profile.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.file.FileConfiguration;
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
    private final List<UUID> matchSpectators = new CopyOnWriteArrayList<>();
    private Map<BlockState, Location> placedBlocks = new HashMap<>();
    private EnumMatchState state = EnumMatchState.STARTING;
    private List<Snapshot> snapshots;
    private MatchRunnable runnable;
    private final Queue queue;
    private final Arena arena;
    private final Kit kit;
    private long startTime;
    private boolean ranked;

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
        Alley.getInstance().getMatchRepository().getMatches().add(this);
    }

    /**
     * Starts the match by setting the state and updating player profiles.
     */
    public void startMatch() {
        if (arena instanceof StandAloneArena) {
            ((StandAloneArena) arena).setActive(true);
        }

        state = EnumMatchState.STARTING;
        runnable = new MatchRunnable(this);
        runnable.runTaskTimer(Alley.getInstance(), 0L, 20L);
        getParticipants().forEach(this::initializeParticipant);
        startTime = System.currentTimeMillis();
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
                setupPlayer(player);
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
                PlayerUtil.reset(player, false);
                player.getInventory().setArmorContents(getKit().getArmor());
                player.getInventory().setContents(getKit().getInventory());
            }
        }
    }

    /**
     * Ends the match.
     */
    public void endMatch() {
        placedBlocks.forEach((blockState, location) -> location.getBlock().setType(Material.AIR));
        placedBlocks.clear();

        getParticipants().forEach(this::finalizeParticipant);
        getMatchSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                removeSpectator(player, false);
            }
        });

        Alley.getInstance().getMatchRepository().getMatches().remove(this);
        runnable.cancel();

        if (arena instanceof StandAloneArena) {
            ((StandAloneArena) arena).setActive(false);
        }
    }

    /**
     * Adds a block to the placed blocks map with the intention to handle block placement and removal.
     *
     * @param blockState The block state to add.
     * @param location   The location of the block.
     */
    public void addBlockToPlacedBlocksMap(BlockState blockState, Location location) {
        placedBlocks.put(blockState, location);
    }

    /**
     * Removes a block from the placed blocks map.
     *
     * @param blockState The block state to remove.
     */
    public void removeBlockFromPlacedBlocksMap(BlockState blockState, Location location) {
        placedBlocks.remove(blockState, location);
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
                    resetPlayerState(player);
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

        if (profile.getParty() == null) {
            Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
            return;
        }

        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.PARTY);
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
    public void createSnapshot(Player loser, Player winner) {
        Snapshot winnerSnapshot = new Snapshot(winner, true);
        winnerSnapshot.setOpponent(loser.getUniqueId());
        snapshots.add(winnerSnapshot);

        Snapshot loserSnapshot = new Snapshot(loser, false);
        loserSnapshot.setOpponent(winner.getUniqueId());
        snapshots.add(loserSnapshot);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {
        if (!(state == EnumMatchState.STARTING || state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer.isDead()) {
            return;
        }

        setParticipantAsDead(player, gamePlayer);
        notifySpectators(player.getName() + " has died");
        //notifyParticipants(player.getName() + " has died");

        player.setVelocity(new Vector());

        handleRoundOrRespawn(player);

        if (getParticipants().size() == 2) {
            handleFinalMatchResult();
        }
    }

    /**
     * Handles the round or respawn of a player.
     *
     * @param player The player to handle the round or respawn of.
     */
    private void handleRoundOrRespawn(Player player) {
        if (canEndRound()) {
            state = EnumMatchState.ENDING_ROUND;
            handleRoundEnd();

            if (canEndMatch()) {
                Player killer = PlayerUtil.getLastAttacker(player);
                if (killer != null) {
                    handleEffects(player, killer);
                }
                state = EnumMatchState.ENDING_MATCH;
            }
            getRunnable().setStage(4);
        } else {
            handleRespawn(player);
        }
    }

    private void handleFinalMatchResult() {
        GameParticipant<MatchGamePlayerImpl> participantA = getParticipants().get(0);
        GameParticipant<MatchGamePlayerImpl> participantB = getParticipants().get(1);

        String winner;
        String loser;

        if (isParticipantDead(participantA) && !isParticipantDead(participantB)) {
            winner = participantB.getPlayers().get(0).getPlayer().getName();
            loser = participantA.getPlayers().get(0).getPlayer().getName();
        } else if (!isParticipantDead(participantA) && isParticipantDead(participantB)) {
            winner = participantA.getPlayers().get(0).getPlayer().getName();
            loser = participantB.getPlayers().get(0).getPlayer().getName();
        } else {
            winner = "No winner, it's a draw!";
            loser = "No loser, it's a draw!";
        }

        this.sendMatchResult(winner, loser);
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
     * Sends the match result message.
     *
     * @param winnerName The name of the winner.
     * @param loserName  The name of the loser.
     */
    private void sendMatchResult(String winnerName, String loserName) {
        FileConfiguration config = ConfigHandler.getInstance().getMessagesConfig();
        
        String winnerCommand = config.getString("match.ended.match-result.winner.command").replace("{winner}", winnerName);
        String winnerHover = config.getString("match.ended.match-result.winner.hover").replace("{winner}", winnerName);
        String loserCommand = config.getString("match.ended.match-result.loser.command").replace("{loser}", loserName);
        String loserHover = config.getString("match.ended.match-result.loser.hover").replace("{loser}", loserName);

        for (String line : ConfigHandler.getInstance().getMessagesConfig().getStringList("match.ended.match-result.format")) {
            if (line.contains("{winner}") && line.contains("{loser}")) {
                String[] parts = line.split("\\{winner}", 2);

                if (parts.length > 1) {
                    String[] loserParts = parts[1].split("\\{loser}", 2);

                    TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                    winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                    winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                    TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                    loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                    loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                    this.sendCombinedSpigotMessage(
                            new TextComponent(CC.translate(parts[0])), 
                            winnerComponent, 
                            new TextComponent(CC.translate(loserParts[0])), 
                            loserComponent, 
                            new TextComponent(loserParts.length > 1 ? CC.translate(loserParts[1]) : "")
                    );
                }
            } else if (line.contains("{winner}")) {
                String[] parts = line.split("\\{winner}", 2);

                TextComponent winnerComponent = new TextComponent(CC.translate(winnerName));
                winnerComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, winnerCommand));
                winnerComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(winnerHover)).create()));

                this.sendCombinedSpigotMessage(
                        new TextComponent(CC.translate(parts[0])), 
                        winnerComponent, 
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else if (line.contains("{loser}")) {
                String[] parts = line.split("\\{loser}", 2);

                TextComponent loserComponent = new TextComponent(CC.translate(loserName));
                loserComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, loserCommand));
                loserComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(CC.translate(loserHover)).create()));

                this.sendCombinedSpigotMessage(
                        new TextComponent(CC.translate(parts[0])), 
                        loserComponent, 
                        new TextComponent(parts.length > 1 ? CC.translate(parts[1]) : "")
                );
            } else {
                this.sendMessage(CC.translate(line));
            }
        }
    }

    /**
     * Sets a participant as dead.
     *
     * @param player     The player to set as dead.
     * @param gamePlayer The game player to set as dead.
     */
    private void setParticipantAsDead(Player player, MatchGamePlayerImpl gamePlayer) {
        if (getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
            if (getParticipant(player).getPlayer().getData().getLives() <= 0) {
                gamePlayer.setDead(true);
            } else {
                getParticipant(player).getPlayers().forEach(participant -> participant.setDead(false));
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
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
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
        if (getMatchSpectators() == null) {
            return;
        }
        matchSpectators.stream()
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(CC.translate(message)));
    }

    public void handleLeaving(Player player) {
        if (!(state == EnumMatchState.STARTING || state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            if (!gamePlayer.isDead()) {
                if (this.kit.isSettingEnabled(KitSettingLivesImpl.class)) {
                    getParticipant(player).getPlayer().getData().setLives(0);
                    handleDeath(player);
                } else {
                    handleDeath(player);
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
        if (!(state == EnumMatchState.STARTING || state == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(true);
            if (!gamePlayer.isDead()) {
                handleDeath(player);
            }
        }
    }

    /**
     * Handles the start of a round.
     */
    public void handleRoundStart() {
        snapshots.clear();
        startTime = System.currentTimeMillis();
    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        startTime = System.currentTimeMillis() - startTime;
        getParticipants().forEach(gameParticipant -> {
            if (gameParticipant.isAllDead()) {
                gameParticipant.getPlayers().forEach(gamePlayer -> {
                    Player player = gamePlayer.getPlayer();
                    if (player != null && !gamePlayer.isDead()) {
                        Snapshot snapshot = new Snapshot(player, true);
                        snapshots.add(snapshot);
                    }
                });
            }
        });

        MatchRegularImpl match = (MatchRegularImpl) this;
        if (match.getParticipantA().getPlayers().size() == 1 && match.getParticipantB().getPlayers().size() == 1) {
            updateSnapshots(match);
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

        matchSpectators.add(player.getUniqueId());
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
        resetPlayerState(player);
        teleportPlayerToSpawn(player);
        matchSpectators.remove(player.getUniqueId());
        if (notify) {
            notifyParticipants(player.getName() + " is no longer spectating the match");
            notifySpectators(player.getName() + " is no longer spectating the match");
        }
    }

    /**
     * Gets the duration of the match.
     *
     * @return The duration of the match.
     */
    public String getDuration() {
        if (state == EnumMatchState.STARTING) return EnumMatchState.STARTING.getDescription();
        if (state == EnumMatchState.ENDING_MATCH) return EnumMatchState.ENDING_MATCH.getDescription();
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

        getMatchSpectators().forEach(uuid -> {
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

        getMatchSpectators().forEach(uuid -> {
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

        getMatchSpectators().forEach(uuid -> {
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