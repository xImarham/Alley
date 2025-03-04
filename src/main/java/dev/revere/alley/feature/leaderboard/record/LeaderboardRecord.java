package dev.revere.alley.feature.leaderboard.record;

import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import lombok.Getter;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
public class LeaderboardRecord {
    private final EnumLeaderboardType type;
    private final List<LeaderboardPlayerData> participants;

    /**
     * Constructor for the LeaderboardRecord class.
     *
     * @param type         The type of the leaderboard.
     * @param participants The participants of the leaderboard.
     */
    public LeaderboardRecord(EnumLeaderboardType type, List<LeaderboardPlayerData> participants) {
        this.type = type;
        this.participants = participants;
    }
}
