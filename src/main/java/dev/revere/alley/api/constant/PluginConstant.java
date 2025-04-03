package dev.revere.alley.api.constant;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class holds constants related to a plugin.
 * It allows for easy access to specific plugin constants and configurations.
 *
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
public class PluginConstant {
    protected final JavaPlugin plugin;

    private final String name;
    private final String version;
    private final String description;
    private final String spigotVersion;

    private final ChatColor mainColor;

    private final String packageDirectory;
    private final String adminPermissionPrefix;
    private final String permissionLackMessage;

    /**
     * Constructor for the PluginConstant class.
     *
     * @param plugin The plugin instance.
     * @param packageDirectory The package directory of the plugin.
     * @param mainColor The main color for the plugin.
     */
    public PluginConstant(JavaPlugin plugin, String packageDirectory, ChatColor mainColor) {
        this.plugin = plugin;

        PluginDescriptionFile pluginDescription = plugin.getDescription();

        this.name = pluginDescription.getName();
        this.version = pluginDescription.getVersion();
        this.description = pluginDescription.getDescription();
        this.spigotVersion = this.getBukkitVersionExact(plugin);

        this.mainColor = mainColor;

        this.packageDirectory = packageDirectory;
        this.adminPermissionPrefix = pluginDescription.getName() + ".admin";
        this.permissionLackMessage = ChatColor.translateAlternateColorCodes('&', "&cNo Permission.");
    }

    /**
     * Get the exact bukkit version of the server.
     *
     * @param plugin the plugin instance.
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact(JavaPlugin plugin) {
        String version = plugin.getServer().getVersion();
        version = version.split("MC: ")[1];
        version = version.split("\\)")[0];
        return version;
    }
}