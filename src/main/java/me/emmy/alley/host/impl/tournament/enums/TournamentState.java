package me.emmy.alley.host.impl.tournament.enums;

import lombok.Getter;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 14/06/2024 - 22:42
 */
@Getter
public enum TournamentState {
    WAITING("Waiting", "Waiting for players to join"),
    COUNTDOWN("Countdown", "Starting the tournament countdown"),
    STARTING("Starting", "Starting the tournament"),
    STARTING_ROUND("Starting Round", "Starting a new round"),
    ENDING_ROUND("Ending Round", "Ending the current round"),
    SPECTATING("Spectating", "Spectating the tournament"),
    ENDING("Ending" , "Ending the tournament")

    ;

    private final String name;
    private final String description;

    TournamentState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
