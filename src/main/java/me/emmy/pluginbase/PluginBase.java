package me.emmy.pluginbase;

import lombok.Getter;
import lombok.Setter;
import me.emmy.pluginbase.commands.PluginBaseCommand;
import me.emmy.pluginbase.commands.admin.spawn.SetSpawnCommand;
import me.emmy.pluginbase.commands.global.ProfileCommand;
import me.emmy.pluginbase.handler.ConfigHandler;
import me.emmy.pluginbase.listeners.PlayerListener;
import me.emmy.pluginbase.utils.chat.CC;
import me.emmy.pluginbase.utils.command.CommandFramework;
import me.emmy.pluginbase.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
@Setter
public class PluginBase extends JavaPlugin {

    @Getter
    public static PluginBase instance;
    private ConfigHandler configHandler;
    private CommandFramework framework;
    private Location spawnLocation;

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        checkDescription();
        registerHandlers();
        registerManagers();
        registerListeners();
        registerCommands();
        loadSpawnLocation();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.on(timeTaken);
    }

    @Override
    public void onDisable() {
        CC.off();
    }

    private void checkDescription() {
        String author = getDescription().getAuthors().get(0);
        String expectedAuthor = "Emma";

        Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bPluginBase&f] Expected author: &a" + expectedAuthor + "&f, Retrieved author: &c" + author));

        if (!author.equalsIgnoreCase(expectedAuthor)) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bPluginBase&f] &4&lAuthor mismatch! Shutting down the server."));
            System.exit(0);
            Bukkit.shutdown();
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&f[&bPluginBase&f] &aNo changes detected!"));
        }
    }

    private void registerHandlers() {
        configHandler = new ConfigHandler();
    }

    private void registerManagers() {
        framework = new CommandFramework(this);
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerCommands() {
        new PluginBaseCommand();
        new SetSpawnCommand();
        new ProfileCommand();
    }

    private void loadSpawnLocation() {
        FileConfiguration config = configHandler.getConfigByName("settings.yml");

        World world = Bukkit.getWorld(config.getString("on-join.teleport.location.world"));
        double x = config.getDouble("on-join.teleport.location.x");
        double y = config.getDouble("on-join.teleport.location.y");
        double z = config.getDouble("on-join.teleport.location.z");
        float yaw = (float) config.getDouble("on-join.teleport.location.yaw");
        float pitch = (float) config.getDouble("on-join.teleport.location.pitch");

        spawnLocation = new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        FileConfiguration config = configHandler.getConfigByName("settings.yml");

        config.set("on-join.teleport.location.world", location.getWorld().getName());
        config.set("on-join.teleport.location.x", location.getX());
        config.set("on-join.teleport.location.y", location.getY());
        config.set("on-join.teleport.location.z", location.getZ());
        config.set("on-join.teleport.location.yaw", location.getYaw());
        config.set("on-join.teleport.location.pitch", location.getPitch());

        configHandler.saveConfig(configHandler.getConfigFileByName("settings.yml"), config);
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
