package me.emmy.alley.utils;

import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;

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
}
