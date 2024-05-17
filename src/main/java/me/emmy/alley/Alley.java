package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.commands.AlleyCommand;
import me.emmy.alley.commands.admin.kit.KitCommand;
import me.emmy.alley.commands.admin.kit.subcommands.*;
import me.emmy.alley.commands.admin.spawn.SetSpawnCommand;
import me.emmy.alley.handler.ConfigHandler;
import me.emmy.alley.hotbar.ItemManager;
import me.emmy.alley.kit.KitManager;
import me.emmy.alley.listeners.PlayerListener;
import me.emmy.alley.spawn.SpawnManager;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.CommandFramework;
import me.emmy.alley.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
@Setter
public class Alley extends JavaPlugin {

    @Getter
    public static Alley instance;
    private ConfigHandler configHandler;
    private CommandFramework framework;
    private ItemManager itemManager;
    private KitManager kitManager;
    private SpawnManager spawnManager;
    private String prefix = "§f[§dAlley§f] &r";

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        checkDescription();
        registerHandlers();
        registerManagers();
        registerListeners();
        registerCommands();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.on(timeTaken);
    }

    @Override
    public void onDisable() {
        kitManager.saveKitsToFile();
        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&aSaved all kits to the config file."));
        CC.off();
    }

    private void checkDescription() {
        String author = getDescription().getAuthors().get(0);
        String expectedAuthor = "Emma";

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Expected author: &a" + expectedAuthor + "&f, Retrieved author: &c" + author));

        if (!author.equalsIgnoreCase(expectedAuthor)) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&4&lAuthor mismatch! Shutting down the server."));
            System.exit(0);
            Bukkit.shutdown();
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&aNo changes detected!"));
        }
    }

    private void registerHandlers() {
        configHandler = new ConfigHandler();
    }

    private void registerManagers() {
        framework = new CommandFramework(this);

        itemManager = new ItemManager();

        kitManager = new KitManager();
        kitManager.loadKitsFromFile();

        spawnManager = new SpawnManager();
        spawnManager.loadSpawnLocation();

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerCommands() {
        new AlleyCommand();
        new SetSpawnCommand();

        new KitCommand();
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitGetInventoryCommand();
        new KitListCommand();
        new KitSaveCommand();
        new KitSetDescriptionCommand();
        new KitSetDisplayNameCommand();
        new KitSetIconCommand();
        new KitSetInventoryCommand();
        new KitSetRuleCommand();
        new KitViewRulesCommand();
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
