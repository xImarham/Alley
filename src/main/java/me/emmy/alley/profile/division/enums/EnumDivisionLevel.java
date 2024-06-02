package me.emmy.alley.profile.division.enums;

import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public enum EnumDivisionLevel {
    LEVEL_1("Level 1"),
    LEVEL_2("Level 2"),
    LEVEL_3("Level 3"),;

    ;

    private final String name;

    /**
     * Constructor for the EnumDivisionLevel
     *
     * @param name the name of the level
     */
    EnumDivisionLevel(String name) {
        this.name = name;
    }
}
