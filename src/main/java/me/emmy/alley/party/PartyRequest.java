package me.emmy.alley.party;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 18:44
 */
@Getter
@Setter
public class PartyRequest {
    private final Player sender;
    private final Player target;

    private final long expireTime;

    /**
     * Constructor for the PartyRequest class.
     *
     * @param sender The player sending the request.
     * @param target The player receiving the request.
     */
    public PartyRequest(Player sender, Player target) {
        this.sender = sender;
        this.target = target;
        this.expireTime = System.currentTimeMillis() + 300000L; // 300 seconds (5 minutes)
    }

    /**
     * Check if the party request has expired.
     *
     * @return True if the party request has expired, false otherwise.
     */
    public boolean hasExpired() {
        return System.currentTimeMillis() > this.expireTime;
    }

    /**
     * Get the remaining time until the party request expires.
     *
     * @return The remaining time until the party request expires.
     */
    public long getRemainingTime() {
        return this.expireTime - System.currentTimeMillis();
    }

    /**
     * Get the remaining time formatted as a string.
     *
     * @return The remaining time formatted as a string.
     */
    public String getRemainingTimeFormatted() {
        long seconds = this.getRemainingTime() / 1000;
        long minutes = seconds / 60;
        return String.format("%02d:%02d", minutes, seconds % 60);
    }
}