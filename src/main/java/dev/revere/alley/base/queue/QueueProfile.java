package dev.revere.alley.base.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
    private final int MAX_RANGE = 220;

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
     * Method to queue the player.
     *
     * @param player The player to queue.
     */
    public void queueRange(Player player) {
        this.ticks++;

        if (player != null) {
            String message = "&b&l" + this.queue.getQueueType() + " " + this.queue.getKit().getName() + " &7┃ &f" + this.getFormattedElapsedTime();
            Alley.getInstance().getReflectionRepository().getReflectionService(ActionBarReflectionService.class).sendMessage(player, message);
        }

        if (this.ticks % this.TICK_THRESHOLD != 0) {
            return;
        }

        if (!this.queue.isRanked()) {
            if (player != null) {
                Arrays.asList(
                        "",
                        "&b&l" + this.queue.getKit().getName(),
                        " &f● &bPing Range: &fN/A",
                        "   &7Searching for match...",
                        ""
                ).forEach(line -> player.sendMessage(CC.translate(line)));
            }
            return;
        }

        if (this.range >= this.MAX_RANGE) {
            return;
        }

        int previousRange = this.range;
        this.range = Math.min(this.range + this.RANGE_INCREMENT, this.MAX_RANGE);

        if (this.range != previousRange) {
            if (player != null) {
                Arrays.asList(
                        "",
                        "&b&l" + this.queue.getKit().getName() + " &6&l" + Symbol.RANKED_STAR + "Ranked",
                        " &f● &bELO Range: &f" + this.getMinimumElo() + " &7&l" + Symbol.ARROW_R + "&f " + this.getMaximumElo(),
                        " &f● &bPing Range: &fN/A",
                        "   " + (this.range == this.MAX_RANGE ? "&c&lRANGE LIMIT REACHED..." : "&7Searching for match..."),
                        ""
                ).forEach(line -> player.sendMessage(CC.translate(line)));
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