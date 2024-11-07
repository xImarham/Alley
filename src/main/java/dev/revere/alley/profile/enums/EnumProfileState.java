package dev.revere.alley.profile.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public enum EnumProfileState {

    LOBBY("Lobby", "The player is in the lobby"),
    WAITING("Waiting", "The player is waiting for a match"),
    PLAYING("Playing", "The player is playing a match"),
    SPECTATING("Spectating", "The player is spectating a match"),
    EDITING("Editing", "The player is editing a kit"),
    PLAYING_TOURNAMENT("Tournament", "The player is in a tournament"),
    PLAYING_EVENT("Event", "The player is in an event"),
    FFA("FFA", "The player is in the FFA lobby"),

    ;

    private final String name;
    private final String description;

    EnumProfileState(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
