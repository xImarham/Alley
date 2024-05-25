package me.emmy.alley.match.enums;

import lombok.Getter;

@Getter
public enum EnumMatchState {

    STARTING("Starting", "The match is starting"),
    RUNNING("Running", "The match is running"),
    ENDING("Ending", "The match is ending"),
    ENDED("Ended", "The match has ended")

    ;

    private final String name;
    private final String description;

    EnumMatchState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
