package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.ArenaRepository;
import me.emmy.alley.arena.listener.ArenaListener;
import me.emmy.alley.commands.AlleyCommand;
import me.emmy.alley.commands.admin.arena.ArenaCommand;
import me.emmy.alley.commands.admin.arena.impl.*;
import me.emmy.alley.commands.admin.essential.SpawnItemsCommand;
import me.emmy.alley.commands.admin.kit.KitCommand;
import me.emmy.alley.commands.admin.kit.impl.*;
import me.emmy.alley.commands.admin.spawn.SetSpawnCommand;
import me.emmy.alley.commands.admin.spawn.SpawnCommand;
import me.emmy.alley.commands.global.queue.RankedCommand;
import me.emmy.alley.commands.global.queue.UnrankedCommand;
import me.emmy.alley.commands.global.settings.SettingsCommand;
import me.emmy.alley.commands.global.stats.LeaderboardCommand;
import me.emmy.alley.commands.global.stats.StatsCommand;
import me.emmy.alley.handler.ConfigHandler;
import me.emmy.alley.hotbar.ItemManager;
import me.emmy.alley.kit.KitRepository;
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
    private SpawnManager spawnManager;
    private KitRepository kitRepository;
    private ArenaRepository arenaRepository;
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
        this.framework = new CommandFramework(this);
        this.itemManager = new ItemManager();

        this.kitRepository = new KitRepository();
        this.kitRepository.loadKits();

        this.arenaRepository = new ArenaRepository();
        this.arenaRepository.loadArenas();

        this.spawnManager = new SpawnManager();
        this.spawnManager.loadSpawnLocation();

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
    }

    private void registerCommands() {
        new AlleyCommand();

        //admin commands
        new SpawnItemsCommand();
        new SetSpawnCommand();
        new SpawnCommand();

        new KitCommand();
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitListCommand();
        new KitGetInvCommand();
        new KitSetInvCommand();
        new KitSetDescriptionCommand();

        new ArenaCenterCommand();
        new ArenaCreateCommand();
        new ArenaCuboidCommand();
        new ArenaDeleteCommand();
        new ArenaAddKitCommand();
        new ArenaKitListCommand();
        new ArenaListCommand();
        new ArenaRemoveKitCommand();
        new ArenaSaveCommand();
        new ArenaSetSpawnCommand();
        new ArenaToggleCommand();
        new ArenaToolCommand();
        new ArenaCommand();

        //player commands
        new UnrankedCommand();
        new RankedCommand();
        new SettingsCommand();
        new LeaderboardCommand();
        new StatsCommand();
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
