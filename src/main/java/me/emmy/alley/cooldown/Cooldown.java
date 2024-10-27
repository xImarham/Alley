package me.emmy.alley.cooldown;

import lombok.Getter;
import me.emmy.alley.cooldown.enums.EnumCooldownType;
import me.emmy.alley.util.TaskUtil;
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

    public Cooldown(EnumCooldownType type, Runnable actionToRun) {
        this.type = type;
        this.actionToRun = actionToRun;
        this.startTime = 0L;
    }

    public long calculateEndTime() {
        return startTime + type.getCooldownDuration();
    }

    public boolean isActive() {
        return calculateEndTime() > System.currentTimeMillis() && cooldownTask != null;
    }

    public void resetCooldown() {
        startTime = System.currentTimeMillis();
        cancelExistingTask();
        startNewCooldownTask();
    }

    public void cancelCooldown() {
        startTime = 0L;
        cancelExistingTask();
    }

    public int remainingTime() {
        return (int) ((calculateEndTime() - System.currentTimeMillis()) / 1000);
    }

    private void cancelExistingTask() {
        if (cooldownTask != null) {
            cooldownTask.cancel();
            cooldownTask = null;
        }
    }

    private void startNewCooldownTask() {
        cooldownTask = TaskUtil.runLaterAsync(() -> {
            actionToRun.run();
            cancelExistingTask();
        }, type.getCooldownDuration() / 50L);
    }
}