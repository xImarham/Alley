package me.emmy.alley.utils;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class Logger {

    public static void logTime(String taskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        runnable.run();
        long end = System.currentTimeMillis();
        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&dAlley&f] &fSuccessfully loaded &d" + taskName + " &fin &d" + (end - start) + "ms&f."));
    }

    public static void logMongoDetails() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("database/database.yml");
        String prefix = Alley.getInstance().getPrefix();

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&fMongo Database"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + " > Host: &d" + config.getString("mongo.uri")));
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + " > Database: &d" + config.getString("mongo.database")));
    }
}
