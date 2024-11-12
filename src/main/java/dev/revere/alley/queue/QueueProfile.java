package dev.revere.alley.queue;

import lombok.Data;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Data
public class QueueProfile {
    private final int TICK_THRESHOLD = 6;
    private final int RANGE_INCREMENT = 10;
    private final int MAX_RANGE = 50;

    private final Queue queue;
    private final UUID uuid;
    private int range;
    private int ticks;
    private int elo;

    private long startTime = System.currentTimeMillis();

    /**
     * Constructor for the QueueProfile class.
     *
     * @param queue The queue.
     * @param uuid  The UUID of the queue.
     */
    public QueueProfile(Queue queue, UUID uuid) {
        this.queue = queue;
        this.uuid = uuid;
    }

    /**
     * Method to queue the range.
     */
    public void queueRange() {
        this.ticks++;

        if (this.ticks % this.TICK_THRESHOLD != 0) {
            return;
        }

        this.range += this.RANGE_INCREMENT;
        if (this.range < this.MAX_RANGE) {
            return;
        }

        this.ticks = 0;
        this.range = 0;

        if (this.queue.isRanked()) {
            Player player = Bukkit.getPlayer(this.uuid);
            if (player != null) {
                player.sendMessage(CC.translate("&aIncreased queue range to 50 due to inactivity. &7(" + getMinimumElo() + " - " + getMaximumElo() + ")"));
            }
        }
    }

    /**
     * Method to get the minimum elo.
     *
     * @return The minimum elo.
     */
    private int getMinimumElo() {
        int minimumElo = this.elo - this.range;
        return Math.max(minimumElo, 0);
    }

    /**
     * Method to get the maximum elo.
     *
     * @return The maximum elo.
     */
    private int getMaximumElo() {
        int max = this.elo + this.range;
        return Math.min(max, 3000);
    }

    /**
     * Method to get the elapsed time.
     *
     * @return The elapsed time.
     */
    public long getElapsedTime() {
        return System.currentTimeMillis() - this.startTime;
    }

    /**
     * Method to get the formatted elapsed time.
     *
     * @return The formatted elapsed time.
     */
    public String getFormattedElapsedTime() {
        long elapsedSeconds = getElapsedTime() / 1000;
        return String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60);
    }
}