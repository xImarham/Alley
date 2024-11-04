package me.emmy.alley.util.logger;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.util.chat.Symbol;
import me.emmy.alley.util.chat.CC;
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
            Bukkit.getConsoleSender().sendMessage(CC.translate("&c" + Symbol.ARROW_R + " &4&l" + Alley.getInstance().getDescription().getName() + " Debug &c" + Symbol.ARROW_L + " &7&o" + message));
        }
    }

    /**
     * Log the time it takes to run a task.
     *
     * @param taskName the name of the task to run
     * @param runnable the task to run
     * @param isRunnable   the task to run is it a runnable
     */
    public void logTime(boolean isRunnable, String taskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        if (isRunnable) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(Alley.getInstance().getPrefix() + "&fSuccessfully ran the &b" + taskName + " &fin &b" + (end - start) + "ms&f."));
            return;
        }

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
        Bukkit.getServer().getConsoleSender().sendMessage(CC.translate("&8[&4" + Alley.getInstance().getDescription().getName() + "&8] &cERROR: " + message + "!"));
    }

    /**
     * Send a message to the console when the plugin is enabled.
     *
     * @param timeTaken The time taken to enable the plugin.
     */
    public void pluginEnabled(long timeTaken) {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("        &b&l" + Alley.getInstance().getDescription().getName() + " &bPractice"));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fAuthors: &b" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fVersion: &b" + Alley.getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fDiscord: &b" + Alley.getInstance().getDescription().getWebsite()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fDescription: &b" + Alley.getInstance().getDescription().getDescription()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fArenas: &b" + Alley.getInstance().getArenaRepository().getArenas().size()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fKits: &b" + Alley.getInstance().getKitRepository().getKits().size()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fFFA Arenas: &b" + Alley.getInstance().getFfaRepository().getMatches().size()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fSpigot: &b" + Bukkit.getName()));
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fVersion: &b" + Alley.getInstance().getBukkitVersionExact()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &bMongoDB &f| &bStatus: &aConnected"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("     &fHost: &b" + ConfigHandler.getInstance().getDatabaseConfig().getString("mongo.uri")));
        Bukkit.getConsoleSender().sendMessage(CC.translate("     &fDatabase: &b" + ConfigHandler.getInstance().getDatabaseConfig().getString("mongo.database")));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("    &fLoaded in &b" + timeTaken + " &bms"));
        Bukkit.getConsoleSender().sendMessage(" ");
    }

    /**
     * Send a message to the console when the plugin is disabled.
     */
    public void pluginDisabled() {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&bAlley&8] &cDisabled."));
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}