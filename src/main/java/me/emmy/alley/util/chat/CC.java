package me.emmy.alley.util.chat;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 12:41
 */
@UtilityClass
public class CC {

    /**
     * Translate a string to a colored string.
     *
     * @param string The string to translate.
     * @return The translated string.
     */
    public String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Translate a list of strings to a colored list of strings.
     *
     * @param string The list of strings to translate.
     * @return The translated list of strings.
     */
    public List<String> translate(List<String> string) {
        List<String> list = new ArrayList<>();

        for (String line : string) {
            list.add(ChatColor.translateAlternateColorCodes('&', line));
        }

        return list;
    }

    /**
     * Translate an array of strings to a colored list of strings.
     *
     * @param string The array of strings to translate.
     * @return The translated list of strings.
     */
    public List<String> translate(String[] string) {
        List<String> list = new ArrayList<>();

        for (String line : string) {
            if (line != null) {
                list.add(ChatColor.translateAlternateColorCodes('&', line));
            }
        }

        return list;
    }

    /**
     * Broadcast a message to all players.
     *
     * @param text The message to send.
     */
    public static void broadcast(String text) {
        Bukkit.broadcastMessage(translate(text));
    }

    public String MENU_BAR = translate("&7&m------------------------");
    //public String FLOWER_BAR = translate("&b&lೋღ&b&l&m«-------&f&l&m-------&b&l&m-------»&r&b&lღೋ");

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
