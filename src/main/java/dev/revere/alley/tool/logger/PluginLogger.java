package dev.revere.alley.tool.logger;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.IPluginConstant;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.base.arena.IArenaService;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.feature.division.IDivisionService;
import dev.revere.alley.feature.knockback.IKnockbackAdapter;
import dev.revere.alley.feature.level.ILevelService;
import dev.revere.alley.feature.title.ITitleService;
import dev.revere.alley.game.ffa.IFFAService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.server.ICoreAdapter;
import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;
import org.bukkit.Server;

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
        Server server = plugin.getServer();

        IPluginConstant constants = plugin.getService(IPluginConstant.class);
        IConfigService configService = plugin.getService(IConfigService.class);
        ICoreAdapter coreAdapter = plugin.getService(ICoreAdapter.class);
        IKnockbackAdapter knockbackAdapter = plugin.getService(IKnockbackAdapter.class);
        IKitService kitService = plugin.getService(IKitService.class);
        IFFAService ffaService = plugin.getService(IFFAService.class);
        IArenaService arenaService = plugin.getService(IArenaService.class);
        IDivisionService divisionService = plugin.getService(IDivisionService.class);
        ITitleService titleService = plugin.getService(ITitleService.class);
        ILevelService levelService = plugin.getService(ILevelService.class);
        IProfileService profileService = plugin.getService(IProfileService.class);

        ChatColor color = constants.getMainColor();
        ChatColor secondary = ChatColor.WHITE;

        Arrays.asList(
                "",
                "        " + color + "&l" + constants.getName() + color + " Practice",
                "",
                "    " + secondary + "Authors: " + color + constants.getAuthors().toString().replace("[", "").replace("]", ""),
                "",
                "    " + secondary + "Version: " + color + constants.getVersion(),
                "    " + secondary + "Description: " + color + constants.getDescription(),
                "",
                "    " + secondary + "Hooked Core: " + color + coreAdapter.getCore().getType().getPluginName(),
                "    " + secondary + "Hooked Knockback: " + color + knockbackAdapter.getKnockbackImplementation().getType().getSpigotName(),
                "",
                "    " + secondary + "Kits: " + color + kitService.getKits().size(),
                "    " + secondary + "FFA Kits: " + color + ffaService.getFfaKits().size(),
                "    " + secondary + "Arenas: " + color + arenaService.getArenas().size(),
                "    " + secondary + "Divisions: " + color + divisionService.getDivisions().size(),
                "    " + secondary + "Titles: " + color + titleService.getTitles().size(),
                "    " + secondary + "Levels: " + color + levelService.getLevels().size(),
                "",
                "    " + color + "MongoDB " + secondary + "| " + color + "Status: &aConnected",
                "     " + secondary + "Database: " + color + configService.getDatabaseConfig().getString("mongo.database"),
                "     " + secondary + "Loaded Profiles: " + color + profileService.getProfiles().size(),
                "",
                "    " + secondary + "Spigot: " + color + server.getName(),
                "    " + secondary + "Version: " + color + constants.getSpigotVersion(),
                "",
                "    " + secondary + "Loaded in " + color + timeTaken + "ms",
                ""
        ).forEach(line -> server.getConsoleSender().sendMessage(CC.translate(line)));
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