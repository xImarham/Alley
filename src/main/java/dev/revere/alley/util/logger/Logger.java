package dev.revere.alley.util.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.util.ServerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

import java.util.Arrays;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@UtilityClass
public class Logger {
    /**
     * Log an exception to the console.
     *
     * @param message   the message to log
     * @param exception the exception to log
     */
    public void logException(String message, Exception exception) {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c" + Symbol.ARROW_R + " &4&l" + Alley.getInstance().getDescription().getName() + " Exception &c" + Symbol.ARROW_L + " &7&o" + message + " &c" + exception.getMessage()));
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

            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully initialized the &b" + taskName + " &fin &b" + (end - start) + "ms&f."));
        } catch (Exception exception) {
            logException("Failed to run the task: " + taskName, exception);
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

            Bukkit.getConsoleSender().sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully ran the &b" + runnableTaskName + " &fin &b" + (end - start) + "ms&f."));
        } catch (Exception exception) {
            logException("Failed to run the task: " + runnableTaskName, exception);
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
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.PREFIX + "&fSuccessfully " + action + "&f the &b" + task + " &fin &b" + runtime + "ms&f."));
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void log(String message) {
        Bukkit.getConsoleSender().sendMessage(CC.translate(CC.PREFIX + message));
    }

    /**
     * Log an error to the console.
     *
     * @param message the error message to log
     */
    public void logError(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&8[&4" + Alley.getInstance().getDescription().getName() + "&8] &cERROR: " + message + "!"));
    }

    /**
     * Send a message to the console when the plugin is enabled.
     *
     * @param timeTaken The time taken to enable the plugin.
     */
    public void pluginEnabled(long timeTaken) {
        Alley plugin = Alley.getInstance();
        Arrays.asList(
                " ",
                "        &b&l" + plugin.getDescription().getName() + " &bPractice",
                " ",
                "    &fAuthors: &b" + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                " ",
                "    &fVersion: &b" + plugin.getDescription().getVersion(),
                "    &fDiscord: &b" + plugin.getDescription().getWebsite(),
                "    &fDescription: &b" + plugin.getDescription().getDescription(),
                " ",
                "    &fKits &b" + plugin.getKitRepository().getKits().size(),
                "    &fArenas: &b" + plugin.getArenaRepository().getArenas().size(),
                "    &fFFA Arenas: &b" + plugin.getFfaRepository().getMatches().size(),
                "    &fDivisions: &b" + plugin.getDivisionRepository().getDivisions().size(),
                " ",
                "    &bMongoDB &f| &bStatus: &aConnected",
                "     &fHost: &b" + plugin.getConfigService().getDatabaseConfig().getString("mongo.uri"),
                "     &fDatabase: &b" + plugin.getConfigService().getDatabaseConfig().getString("mongo.database"),
                "     &fLoaded Profiles: &b" + plugin.getProfileRepository().getProfiles().size(),
                " ",
                "    &fSpigot: &b" + Bukkit.getName(),
                "    &fVersion: &b" + ServerUtil.getBukkitVersionExact(plugin),
                " ",
                "    &fLoaded in &b" + timeTaken + " &bms",
                " "
        ).forEach(line -> Bukkit.getConsoleSender().sendMessage(CC.translate(line)));
    }

    /**
     * Send a message to the console when the plugin is disabled.
     */
    public void pluginDisabled() {
        Arrays.asList(
                "",
                CC.PREFIX + "&cDisabled.",
                ""
        ).forEach(line -> Bukkit.getConsoleSender().sendMessage(CC.translate(line)));
    }
}