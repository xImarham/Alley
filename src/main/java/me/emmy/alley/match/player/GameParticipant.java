package me.emmy.alley.match.player;

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

    /**
     * Constructor for the GameParticipant class.
     *
     * @param player The player.
     */
    public GameParticipant(T player) {
        this.player = player;
    }

    public List<T> getPlayers() {
        return Collections.singletonList(player);
    }

    /**
     * Checks if all the players in the participant are dead.
     *
     * @return True if all the players are dead.
     */
    public boolean isAllDead() {
        return player.isDead();
    }

    /**
     * Checks if the participant contains a player.
     *
     * @param uuid The UUID of the player.
     * @return True if the participant contains the player.
     */
    public boolean containsPlayer(UUID uuid) {
        return player.getUuid().equals(uuid);
    }
}
