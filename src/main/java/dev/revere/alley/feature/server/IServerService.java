package dev.revere.alley.feature.server;

import dev.revere.alley.plugin.lifecycle.IService;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IServerService extends IService {
    /**
     * Checks if players are currently allowed to join matchmaking queues.
     *
     * @return true if queueing is allowed, false otherwise.
     */
    boolean isQueueingAllowed();

    /**
     * Sets whether players are allowed to join matchmaking queues.
     *
     * @param allowed The new queueing status.
     */
    void setQueueingAllowed(boolean allowed);

    /**
     * Forcefully ends all active matches on the server.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void endAllMatches(Player issuer);

    /**
     * Disbands all active parties on the server.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void disbandAllParties(Player issuer);

    /**
     * Removes all players from all matchmaking queues.
     *
     * @param issuer The staff member who initiated this action (can be null for console).
     */
    void clearAllQueues(Player issuer);
}
