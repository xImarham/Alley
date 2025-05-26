package dev.revere.alley.base.kit.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
@Getter
public enum EnumKitCategory {
    NORMAL("Normal", "Regularly-accessible modes."),
    EXTRA("Extra", "Less-popular modes."),

    ;

    private final String name;
    private final String description;

    /**
     * Constructor for the EnumKitCategory enum.
     *
     * @param name        The name of the kit category.
     * @param description The description of the kit category.
     */
    EnumKitCategory(String name, String description) {
        this.name = name;
        this.description = description;
    }
}