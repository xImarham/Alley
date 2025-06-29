package dev.revere.alley.tool.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@UtilityClass
public class PluginLogger {
    /**
     * Send a message to the console when the plugin is enabled.
     *
     * @param timeTaken The time taken to enable the plugin.
     */
    public void onEnable(long timeTaken) {
        Alley plugin = Alley.getInstance();

        PluginConstant pluginConstant = plugin.getPluginConstant();
        ChatColor color = pluginConstant.getMainColor();
        ChatColor secondary = ChatColor.WHITE;

        Arrays.asList(
                "",
                "        " + color + "&l" + pluginConstant.getName() + color + " Practice",
                "",
                "    " + secondary + "Authors: " + color + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                "",
                "    " + secondary + "Version: " + color + pluginConstant.getVersion(),
                "    " + secondary + "Discord: " + color + plugin.getDescription().getWebsite(),
                "    " + secondary + "Description: " + color + pluginConstant.getDescription(),
                "",
                "    " + secondary + "Hooked Core: " + color + plugin.getCoreAdapter().getCore().getType().getPluginName(),
                "    " + secondary + "Hooked Knockback: " + color + plugin.getKnockbackAdapter().getKnockbackType().getType().getSpigotName(),
                "",
                "    " + secondary + "Kits " + color + plugin.getKitService().getKits().size(),
                "    " + secondary + "FFA Kits: " + color + plugin.getFfaService().getFfaKits().size(),
                "    " + secondary + "Arenas: " + color + plugin.getArenaService().getArenas().size(),
                "    " + secondary + "Divisions: " + color + plugin.getDivisionService().getDivisions().size(),
                "    " + secondary + "Titles: " + color + plugin.getTitleService().getTitles().size(),
                "    " + secondary + "Levels: " + color + plugin.getLevelService().getLevels().size(),
                "",
                "    " + color + "MongoDB " + secondary + "| " + color + "Status: &aConnected",
                "     " + secondary + "Host: " + color + plugin.getConfigService().getDatabaseConfig().getString("mongo.uri"),
                "     " + secondary + "Database: " + color + plugin.getConfigService().getDatabaseConfig().getString("mongo.database"),
                "     " + secondary + "Loaded Profiles: " + color + plugin.getProfileService().getProfiles().size(),
                "",
                "    " + secondary + "Spigot: " + color + plugin.getServer().getName(),
                "    " + secondary + "Version: " + color + pluginConstant.getBukkitVersionExact(plugin),
                "",
                "    " + secondary + "Loaded in " + color + timeTaken + " " + color + "ms",
                ""
        ).forEach(line -> plugin.getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }

    public void onDisable() {
        Alley plugin = Alley.getInstance();
        Arrays.asList(
                "",
                CC.PREFIX + "&cDisabled.",
                ""
        ).forEach(line -> plugin.getServer().getConsoleSender().sendMessage(CC.translate(line)));
    }
}