package me.emmy.alley.profile.enums;

import lombok.Getter;

@Getter
public enum EnumProfileState {

    LOBBY("Lobby", "The player is in the lobby"),
    WAITING("Waiting", "The player is waiting for a match"),
    PLAYING("Playing", "The player is playing a match"),
    PLAYING_FFA("Playing FFA", "The player is playing in FFA."),
    SPECTATING("Spectating", "The player is spectating a match"),
    EDITING("Editing", "The player is editing a kit"),
    FFA("FFA", "The player is in the FFA lobby"),

    ;

    private final String name;
    private final String description;

    EnumProfileState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
