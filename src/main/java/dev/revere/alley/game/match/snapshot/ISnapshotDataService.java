package dev.revere.alley.game.match.snapshot;

import dev.revere.alley.core.lifecycle.IService;

import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ISnapshotDataService extends IService {
    /**
     * Checks if a player is currently performing a "W-Tap" based on
     * recent sprint packet data.
     *
     * @param uuid The UUID of the player to check.
     * @return true if the player is considered to be W-Tapping.
     */
    boolean isWTap(UUID uuid);

    /**
     * Manually resets the sprint state for a player, used for correcting
     * the W-Tap detection logic.
     *
     * @param uuid The UUID of the player.
     */
    void resetSprint(UUID uuid);

    /**
     * Clears all tracked sprint data for a player, typically after a match ends.
     *
     * @param uuid The UUID of the player.
     */
    void clearData(UUID uuid);
}