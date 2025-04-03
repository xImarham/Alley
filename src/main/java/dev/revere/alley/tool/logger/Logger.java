package dev.revere.alley.tool.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.command.ConsoleCommandSender;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@UtilityClass
public class Logger {
    private final static ConsoleCommandSender consoleSender;

    static {
        consoleSender = Alley.getInstance().getServer().getConsoleSender();
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void log(String message) {
        consoleSender.sendMessage(CC.translate(CC.PREFIX + message));
    }

    /**
     * Log an error to the console.
     *
     * @param message the error message to log
     */
    public void logError(String message) {
        consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&c(ERROR) &8" + message));
    }

    /**
     * Log an exception to the console.
     *
     * @param message   the info message or the class name
     * @param exception the exception to log
     */
    public void logException(String message, Exception exception) {
        consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&c(EXCEPTION) &f" + message + ": &r" + exception.getMessage() + "!"));
    }

    /**
     * Log the time it takes to run a task.
     *
     * @param taskName the name of the task to run
     * @param runnable the task to run
     */
    public void logTime(String taskName, Runnable runnable) {
        try {
            long start = System.currentTimeMillis();
            runnable.run();
            long end = System.currentTimeMillis();

            consoleSender.sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully initialized the &b" + taskName + " &fin &b" + (end - start) + "ms&f."));
        } catch (Exception exception) {
            logException("Failed to run the " + taskName + " task", exception);
        }
    }

    /**
     * Log the time it takes to run a task.
     *
     * @param runnableTaskName the name of the task to run
     * @param runnable         the task to run
     */
    public void logTimeTask(String runnableTaskName, Runnable runnable) {
        try {
            long start = System.currentTimeMillis();
            runnable.run();
            long end = System.currentTimeMillis();

            consoleSender.sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully ran the &b" + runnableTaskName + " &fin &b" + (end - start) + "ms&f."));
        } catch (Exception exception) {
            logException("Failed to run the " + runnableTaskName + " task", exception);
        }
    }

    /**
     * Measure the runtime of a task and log it to the console with the provided action in its parameter.
     *
     * @param action   the action
     * @param task     the task to measure
     * @param runnable the runnable to run
     */
    public void logTimeWithAction(String action, String task, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long runtime = System.currentTimeMillis() - start;
        consoleSender.sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully " + action + "&f the &b" + task + " &fin &b" + runtime + "ms&f."));
    }
}