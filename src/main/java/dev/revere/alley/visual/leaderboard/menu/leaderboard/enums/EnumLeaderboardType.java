package dev.revere.alley.visual.leaderboard.menu.leaderboard.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@Getter
@AllArgsConstructor
public enum EnumLeaderboardType {
    UNRANKED("Unranked"),
    RANKED("Ranked"),
    FFA("FFA")

    ;

    private final String name;
}
