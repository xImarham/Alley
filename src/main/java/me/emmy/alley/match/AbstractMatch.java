package me.emmy.alley.match;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.profile.Profile;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class AbstractMatch {

    private EnumMatchState state = EnumMatchState.STARTING;
    private final Arena arena;
    private final Kit kit;

    private List<UUID> spectators;

    /**
     * Constructor for the AbstractMatch class.
     *
     * @param kit   The kit of the match.
     * @param arena The arena of the match.
     */
    public AbstractMatch(Kit kit, Arena arena) {
        this.kit = kit;
        this.arena = arena;

        Alley.getInstance().getMatchRepository().getMatches().add(this);
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

        spectators.add(player.getUniqueId());
    }

    /**
     * Removes a player from the list of spectators.
     *
     * @param player The player to remove.
     * @param target The player to stop spectating.
     */
    public void removeSpectator(Player player, Player target) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setMatch(null);

        spectators.remove(player.getUniqueId());
    }

    /**
     * Starts the match.
     */
    public void startMatch() {

    }

    /**
     * Ends the match.
     */
    public void endMatch() {

    }

    /**
     * Handles the death of a player.
     *
     * @param player The player that died.
     */
    public void handleDeath(Player player) {

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

    }

    public MatchGamePlayerImpl getGamePlayer(Player player) {
        return getParticipants().stream().filter(gameParticipant -> gameParticipant.getPlayers().get(0).getUuid().equals(player.getUniqueId())).findFirst().map(gameParticipant -> gameParticipant.getPlayers().get(0)).orElse(null);
    }

    public abstract List<GameParticipant<MatchGamePlayerImpl>> getParticipants();
    public abstract boolean canEndRound();
    public abstract boolean canEndMatch();
}
