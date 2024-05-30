package me.emmy.alley.tournament;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:31
 */

@Getter
@Setter
public class TournamentType {
    private String name;
    private boolean rewards;

    /**
     * Constructor for the TournamentType class.
     *
     * @param name    The name of the tournament type.
     * @param rewards Whether the tournament type rewards players.
     */
    public TournamentType(String name, boolean rewards) {
        this.name = name;
        this.rewards = rewards;
    }
}
