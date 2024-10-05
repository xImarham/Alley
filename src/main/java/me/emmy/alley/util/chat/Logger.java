package me.emmy.alley.util.chat;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.util.Symbol;
import org.bukkit.Bukkit;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@UtilityClass
public class Logger {
    /**
     * Log a debug message to the console based on the boolean value in the settings.yml.
     *
     * @param message the debug message to log
     */
    public void debug(String message) {
        if (ConfigHandler.getInstance().getSettingsConfig().getBoolean("debugging")) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c" + Symbol.ARROW_R.getSymbol() + " &4&lAlley Debug &c" + Symbol.ARROW_L.getSymbol() + " &7&o" + message));
        }
    }

    /**
     * Log the time it takes to run a task.
     *
     * @param taskName the name of the task
     * @param runnable the task to run
     */
    public void logTime(String taskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        Bukkit.getConsoleSender().sendMessage(CC.translate(Alley.getInstance().getPrefix() + "&fSuccessfully loaded &b" + taskName + " &fin &b" + (end - start) + "ms&f."));
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(Alley.getInstance().getPrefix() + message));
    }

    /**
     * Log an error to the console.
     *
     * @param message the error message to log
     */
    public void logError(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&8[&4Alley&8] &cERROR: " + message + "!"));
    }
}
