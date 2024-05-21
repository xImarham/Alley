package me.emmy.alley.match.player;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
}
