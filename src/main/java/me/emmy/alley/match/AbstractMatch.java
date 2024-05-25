package me.emmy.alley.match;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.match.runnable.MatchRunnable;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.PlayerUtil;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public abstract class AbstractMatch {

    private final List<UUID> matchSpectators = new CopyOnWriteArrayList<>();
    private EnumMatchState matchState = EnumMatchState.STARTING;
    private final Queue matchQueue;
    private final Arena matchArena;
    private final Kit matchKit;

    private MatchRunnable matchRunnable;

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
            Player player = Alley.getInstance().getServer().getPlayer(gamePlayer.getUuid());
            if (player != null) {
                resetPlayerState(player);
                Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
                profile.setState(EnumProfileState.LOBBY);
                profile.setMatch(null);
                teleportPlayerToSpawn(player);
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

        if (canEndRound()) {
            matchState = EnumMatchState.ENDING;
            handleRoundEnd();

            if (canEndMatch()) {
                matchState = EnumMatchState.ENDED;
                endMatch();
            }
            getMatchRunnable().setStage(4);
        }
    }

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

    }

    /**
     * Handles the end of a round.
     */
    public void handleRoundEnd() {
        if (!canEndMatch()) {
            matchState = EnumMatchState.ENDING;
            matchRunnable.setStage(3);
        }
    }

    /**
     * Adds a player to the list of spectators.
     *
     * @param player The player to add.
     * @param target The player to spectate.
     */
    public void addSpectator(Player player, Player target) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.SPECTATING);
        profile.setMatch(this);
        matchSpectators.add(player.getUniqueId());
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove from spectating.
     */
    public void removeSpectator(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);
        resetPlayerState(player);
        teleportPlayerToSpawn(player);
        matchSpectators.remove(player.getUniqueId());
    }

    private MatchGamePlayerImpl getGamePlayer(Player player) {
        return getParticipants().stream()
                .map(GameParticipant::getPlayers)
                .flatMap(List::stream)
                .filter(gamePlayer -> gamePlayer.getUuid().equals(player.getUniqueId()))
                .findFirst()
                .orElse(null);
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
