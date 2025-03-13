package dev.revere.alley.feature.queue.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 13/03/2025
 */
@Getter
public enum EnumQueueType {
    UNRANKED("Solo Unranked Queue"),
    DUOS("Duo Unranked Queue"),
    BOTS("Bots Unranked Queue"),
    FFA("FFA Unranked Queue"),

    ;

    private final String menuTitle;

    /**
     * Constructor for the EnumQueueType class.
     *
     * @param menuTitle The title of the menu.
     */
    EnumQueueType(String menuTitle) {
        this.menuTitle = menuTitle;
    }
}