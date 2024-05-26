package me.emmy.alley.match;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.match.impl.MatchRegularImpl;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.match.runnable.MatchRunnable;
import me.emmy.alley.match.snapshot.Snapshot;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public abstract class AbstractMatch {
    private final List<UUID> matchSpectators = new CopyOnWriteArrayList<>();
    private EnumMatchState matchState = EnumMatchState.STARTING;
    private MatchRunnable matchRunnable;
    private List<Snapshot> snapshots;
    private final Queue matchQueue;
    private final Arena matchArena;
    private final Kit matchKit;
    private long startTime;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param matchQueue The queue of the match.
     * @param matchKit   The kit of the match.
     * @param matchArena The matchArena of the match.
     */
    public AbstractMatch(Queue matchQueue, Kit matchKit, Arena matchArena) {
        this.matchQueue = matchQueue;
        this.matchKit = matchKit;
        this.matchArena = matchArena;
        this.snapshots = new ArrayList<>();
        Alley.getInstance().getMatchRepository().getMatches().add(this);
    }

    /**
     * Starts the match by setting the state and updating player profiles.
     */
    public void startMatch() {
        matchState = EnumMatchState.STARTING;
        matchRunnable = new MatchRunnable(this);
        matchRunnable.runTaskTimer(Alley.getInstance(), 0L, 20L);
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
                PlayerUtil.reset(player);

                player.getInventory().setArmorContents(getMatchKit().getArmor());
                player.getInventory().setContents(getMatchKit().getInventory());
            }
        }
    }

    /**
     * Ends the match.
     */
    public void endMatch() {
        getParticipants().forEach(this::finalizeParticipant);
        getMatchSpectators().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid);
            if (player != null) {
                removeSpectator(player, false);
            }
        });

        Alley.getInstance().getMatchRepository().getMatches().remove(this);
        matchRunnable.cancel();
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
        Alley.getInstance().getSpawnHandler().teleportToSpawn(player);
        Alley.getInstance().getHotbarUtility().applySpawnItems(player);
    }

    /**
     * Teleports a player to the spawn.
     *
     * @param player The player to teleport.
     */
    private void resetPlayerState(Player player) {
        player.setFireTicks(0);
        player.updateInventory();
        PlayerUtil.reset(player);
    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {
        if (!(matchState == EnumMatchState.STARTING || matchState == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer.isDead()) {
            return;
        }

        gamePlayer.setDead(true);
        notifySpectators(player.getName() + " has died");
        notifyParticipants(player.getName() + " has died");

        player.setVelocity(new Vector());

        Snapshot snapshot = new Snapshot(player, false);
        snapshots.add(snapshot);

        if (canEndRound()) {
            matchState = EnumMatchState.ENDING_ROUND;
            handleRoundEnd();

            if (canEndMatch()) matchState = EnumMatchState.ENDING_MATCH;
            getMatchRunnable().setStage(4);
        } else {
            handleRespawn(player);
        }
    }

    /**
     * Notifies the participants of a message.
     *
     * @param message The message to notify.
     */
    private void notifyParticipants(String message) {
        getParticipants().forEach(gameParticipant -> gameParticipant.getPlayers().forEach(uuid -> {
            Player player = Alley.getInstance().getServer().getPlayer(uuid.getUuid());
            if (player != null) {
                player.sendMessage(message);
            }
        }));
    }

    /**
     * Notifies the spectators of a message.
     *
     * @param message The message to notify.
     */
    private void notifySpectators(String message) {
        matchSpectators.stream()
                .map(uuid -> Alley.getInstance().getServer().getPlayer(uuid))
                .filter(Objects::nonNull)
                .forEach(player -> player.sendMessage(message));
    }

    /**
     * Handles the respawn of a player.
     *
     * @param player The player that respawned.
     */
    public void handleRespawn(Player player) {

    }

    /**
     * Handles the disconnect of a player.
     *
     * @param player The player that disconnected.
     */
    public void handleDisconnect(Player player) {
        if (!(matchState == EnumMatchState.STARTING || matchState == EnumMatchState.RUNNING)) return;

        MatchGamePlayerImpl gamePlayer = getGamePlayer(player);
        if (gamePlayer != null) {
            gamePlayer.setDisconnected(false);
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
        if (!canEndMatch()) {
            matchState = EnumMatchState.ENDING_ROUND;
            matchRunnable.setStage(3);
        }

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
        Alley.getInstance().getHotbarUtility().applySpectatorItems(player);

        if (matchArena.getCenter() == null) {
            player.sendMessage(CC.translate("&cThe arena is not set up for spectating"));
            return;
        }

        player.teleport(matchArena.getCenter());
        player.spigot().setCollidesWithEntities(false);
        player.setGameMode(GameMode.CREATIVE);
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
        if (matchState == EnumMatchState.STARTING) return EnumMatchState.STARTING.getDescription();
        if (matchState == EnumMatchState.ENDING_MATCH) return EnumMatchState.ENDING_MATCH.getDescription();
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
