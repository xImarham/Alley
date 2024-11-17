package dev.revere.alley.leaderboard.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 14:13
 */
@Getter
public enum EnumLeaderboardType {
    UNRANKED("Unranked (All time)"),
    UNRANKED_MONTHLY("Unranked (Monthly)"),
    WIN_STREAK("Win Streak"),
    FFA("FFA"),
    RANKED("Ranked"),
    TOURNAMENT("Tournament")

    ;

    private final String name;

    /**
     * Constructor for the EnumLeaderboardType.
     *
     * @param name The name of the leaderboard type.
     */
    EnumLeaderboardType(String name) {
        this.name = name;
    }
}