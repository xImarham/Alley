package dev.revere.alley.feature.leaderboard;

import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.core.lifecycle.IService;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ILeaderboardService extends IService {
    /**
     * Gets the sorted leaderboard data for a specific kit and type.
     * <p>
     * Note: This data is typically calculated once on startup.
     *
     * @param kit  The kit to get the leaderboard for.
     * @param type The type of leaderboard (e.g., ELO).
     * @return A sorted list of LeaderboardPlayerData, or an empty list if not found.
     */
    List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, EnumLeaderboardType type);

    /**
     * Recalculates all leaderboards from the current profile data.
     * This is a heavy operation and should be used sparingly.
     */
    void recalculateLeaderboards();

}