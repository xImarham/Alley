package me.emmy.alley.utils.chat;

import me.emmy.alley.Alley;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 12:41
 */
public class CC {
    /**
     * Translate a string to a colored string.
     *
     * @param string The string to translate.
     * @return The translated string.
     */
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translate a list of strings to a colored list of strings.
     *
     * @param lines The list of strings to translate.
     * @return The translated list of strings.
     */
    public static List<String> translate(List<String> lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return toReturn;
    }

    /**
     * Translate an array of strings to a colored list of strings.
     *
     * @param lines The array of strings to translate.
     * @return The translated list of strings.
     */
    public static List<String> translate(String[] lines) {
        List<String> toReturn = new ArrayList<>();

        for (String line : lines) {
            if (line != null) {
                toReturn.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return toReturn;
    }

    /**
     * Broadcast a message to all players.
     *
     * @param text The message to send.
     */
    public static void broadcast(String text) {
        Bukkit.broadcastMessage(translate(text));
    }

    public static String MENU_BAR = translate("&7&m------------------------");
    public static String FLOWER_BAR = translate("&d&lೋღ&d&l&m«-------&f&l&m-------&d&l&m-------»&r&d&lღೋ");
    public static String FLOWER_BAR_LONG = translate("&b&lೋღ&b&l&m«-------&f&l&m-----------------&b&l&m-------»&r&b&lღೋ");
    public static String FLOWER_BAR_VERY_LONG = translate("&b&lೋღ&b&l&m«-------&f&l&m----------------------------&b&l&m-------»&r&b&lღೋ");
    public static String FLOWER_BAR_LONG_RED = translate("&4&lೋღ&4&l&m«-------&f&l&m-----------------&4&l&m-------»&r&4&lღೋ");

    /**
     * Send a message to the console when the plugin is enabled.
     *
     * @param timeTaken The time taken to enable the plugin.
     */
    public static void pluginEnabled(long timeTaken) {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8&m-----------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Plugin: &d" + Alley.getInstance().getDescription().getName() + " &dPractice"));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Authors: &d" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Version: &d" + Alley.getInstance().getDescription().getVersion()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Link: &d" + Alley.getInstance().getDescription().getWebsite()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Arenas: &d" + Alley.getInstance().getArenaRepository().getArenas().size()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Kits: &d" + Alley.getInstance().getKitRepository().getKits().size()));
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| FFA Arenas: &d" + Alley.getInstance().getFfaRepository().getMatches().size()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Spigot: &d" + Bukkit.getName()));
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate(" &f| Load time: &d" + (timeTaken) + " &dms"));
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8&m-----------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(" ");
    }

    /**
     * Send a message to the console when the plugin is disabled.
     */
    public static void pluginDisabled() {
        Bukkit.getConsoleSender().sendMessage(" ");
        Bukkit.getConsoleSender().sendMessage(CC.translate("&8[&dAlley&8] &fDisabled &dAlley Practice&f!"));
        Bukkit.getConsoleSender().sendMessage(" ");
    }
}
