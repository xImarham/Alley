package dev.revere.alley.cooldown;

import lombok.Getter;
import dev.revere.alley.cooldown.enums.EnumCooldownType;
import dev.revere.alley.util.TaskUtil;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class Cooldown {
    private final EnumCooldownType type;
    private final Runnable actionToRun;
    private long startTime;
    private BukkitTask cooldownTask;

    /**
     * Constructor for the Cooldown class.
     *
     * @param type         the type of cooldown
     * @param actionToRun  the action to run
     */
    public Cooldown(EnumCooldownType type, Runnable actionToRun) {
        this.type = type;
        this.actionToRun = actionToRun;
        this.startTime = 0L;
    }

    /**
     * Calculate the end time of the cooldown.
     *
     * @return the end time of the cooldown
     */
    public long calculateEndTime() {
        return startTime + type.getCooldownDuration();
    }

    /**
     * Check if the cooldown is active.
     *
     * @return true if the cooldown is active, false otherwise
     */
    public boolean isActive() {
        return calculateEndTime() > System.currentTimeMillis() && cooldownTask != null;
    }

    /**
     * Reset the cooldown time and start a new task.
     */
    public void resetCooldown() {
        startTime = System.currentTimeMillis();
        cancelExistingTask();
        startNewCooldownTask();
    }

    /**
     * Cancel the cooldown.
     */
    public void cancelCooldown() {
        startTime = 0L;
        cancelExistingTask();
    }

    /**
     * Get the remaining time of the cooldown.
     *
     * @return the remaining time of the cooldown
     */
    public int remainingTime() {
        return (int) ((calculateEndTime() - System.currentTimeMillis()) / 1000);
    }

    /**
     * Cancel the existing task.
     */
    private void cancelExistingTask() {
        if (cooldownTask != null) {
            cooldownTask.cancel();
            cooldownTask = null;
        }
    }

    /**
     * Start a new cooldown task.
     */
    private void startNewCooldownTask() {
        cooldownTask = TaskUtil.runLaterAsync(() -> {
            actionToRun.run();
            cancelExistingTask();
        }, type.getCooldownDuration() / 50L);
    }
}