package dev.revere.alley.game.match.player.participant;

import dev.revere.alley.game.match.player.GamePlayer;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class GameParticipant<T extends GamePlayer> {
    private T player;
    private boolean eliminated;

    /**
     * Constructor for the GameParticipant class.
     *
     * @param player The player.
     */
    public GameParticipant(T player) {
        this.player = player;
    }

    /**
     * Gets the player associated with the participant.
     *
     * @return The player associated with the participant.
     */
    public List<T> getPlayers() {
        return Collections.singletonList(this.player);
    }

    public int getPlayerSize() {
        return 0; // 0 since this is the solo representation of a participant.
    }

    /**
     * Gets the amount of players that are alive.
     *
     * @return The amount of players that are alive.
     */
    public int getAlivePlayerSize() {
        return this.player.isDead() ? 0 : 1;
    }

    /**
     * Gets the conjoined names of the players in the participant.
     *
     * @return The conjoined names of the players in the participant.
     */
    public String getConjoinedNames() {
        return this.player.getUsername();
    }

    /**
     * Checks if all the players in the participant are dead.
     *
     * @return True if all the players are dead.
     */
    public boolean isAllDead() {
        return this.player.isDead();
    }

    /**
     * Checks if the participant contains a player.
     *
     * @param uuid The UUID of the player.
     * @return True if the participant contains the player.
     */
    public boolean containsPlayer(UUID uuid) {
        return this.player.getUuid().equals(uuid);
    }
}