package me.emmy.alley.match.enums;

import lombok.Getter;

@Getter
public enum EnumMatchState {

    STARTING("Starting", "The match is starting"),
    RUNNING("Running", "The match is running"),
    ENDING_ROUND("Ending Round", "The round is ending"),
    ENDING_MATCH("Ending Match", "The match is ending")

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumMatchState enum.
     *
     * @param name        The name of the match state.
     * @param description The description of the match state.
     */
    EnumMatchState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
