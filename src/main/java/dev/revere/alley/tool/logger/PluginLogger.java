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

        Arrays.asList(
                " ",
                "        " + color + "&l" + pluginConstant.getName() + color + " Practice",
                " ",
                "    &fAuthors: " + color + plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""),
                " ",
                "    &fVersion: " + color + pluginConstant.getVersion(),
                "    &fDiscord: " + color + plugin.getDescription().getWebsite(),
                "    &fDescription: " + color + pluginConstant.getDescription(),
                " ",
                "    &fKits " + color + plugin.getKitService().getKits().size(),
                "    &fArenas: " + color + plugin.getArenaService().getArenas().size(),
                "    &fFFA Arenas: " + color + plugin.getFfaService().getMatches().size(),
                "    &fDivisions: " + color + plugin.getDivisionService().getDivisions().size(),
                " ",
                "    &bMongoDB &f| &bStatus: &aConnected",
                "     &fHost: " + color + plugin.getConfigService().getDatabaseConfig().getString("mongo.uri"),
                "     &fDatabase: " + color + plugin.getConfigService().getDatabaseConfig().getString("mongo.database"),
                "     &fLoaded Profiles: " + color + plugin.getProfileService().getProfiles().size(),
                " ",
                "    &fSpigot: " + color + plugin.getServer().getName(),
                "    &fVersion: " + color + pluginConstant.getBukkitVersionExact(plugin),
                " ",
                "    &fLoaded in " + color + timeTaken + " &bms",
                " "
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