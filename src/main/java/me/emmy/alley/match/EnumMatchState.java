package me.emmy.alley.match;

import lombok.Getter;

@Getter
public enum EnumMatchState {

    STARTING("Starting", "The match is starting"),
    RUNNING("Running", "The match is running"),
    ENDING("Ending", "The match is ending"),

    ;

    private final String name;
    private final String description;

    EnumMatchState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
