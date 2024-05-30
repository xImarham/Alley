package me.emmy.alley.tournament.enums;

import lombok.Getter;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:05
 */

@Getter
public enum EnumTournamentState {
    WAITING("Waiting", "Waiting for the tournament to start"),
    STARTING("Starting", "The tournament starts"),
    FIGHTING("Running", "Players are dueling"),
    ENDING("Ending Match", "The tournament is ending")

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumTournamentState enum.
     *
     * @param name        The name of the tournament state.
     * @param description The description of the tournament state.
     */
    EnumTournamentState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
