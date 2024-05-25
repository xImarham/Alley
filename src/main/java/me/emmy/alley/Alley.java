package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.ArenaRepository;
import me.emmy.alley.arena.listener.ArenaListener;
import me.emmy.alley.commands.AlleyCommand;
import me.emmy.alley.arena.command.ArenaCommand;
import me.emmy.alley.arena.command.impl.*;
import me.emmy.alley.commands.admin.management.PlaytimeCommand;
import me.emmy.alley.commands.admin.essential.SpawnItemsCommand;
import me.emmy.alley.kit.command.KitCommand;
import me.emmy.alley.kit.command.impl.*;
import me.emmy.alley.spawn.command.SetSpawnCommand;
import me.emmy.alley.spawn.command.SpawnCommand;
import me.emmy.alley.queue.command.RankedCommand;
import me.emmy.alley.queue.command.UnrankedCommand;
import me.emmy.alley.party.command.PartyCommand;
import me.emmy.alley.party.command.impl.PartyChatCommand;
import me.emmy.alley.party.command.impl.PartyCreateCommand;
import me.emmy.alley.party.command.impl.PartyInfoCommand;
import me.emmy.alley.party.command.impl.PartyLeaveCommand;
import me.emmy.alley.settings.command.SettingsCommand;
import me.emmy.alley.leaderboard.command.LeaderboardCommand;
import me.emmy.alley.leaderboard.command.StatsCommand;
import me.emmy.alley.database.MongoService;
import me.emmy.alley.database.profile.impl.MongoProfileImpl;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.scoreboard.handler.ScoreboardHandler;
import me.emmy.alley.hotbar.listener.HotbarListener;
import me.emmy.alley.hotbar.HotbarUtility;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.match.MatchRepository;
import me.emmy.alley.match.command.SpectateCommand;
import me.emmy.alley.match.listener.MatchListener;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.listener.PartyListener;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.listener.ProfileListener;
import me.emmy.alley.queue.QueueRepository;
import me.emmy.alley.queue.command.LeaveQueueCommand;
import me.emmy.alley.scoreboard.ScoreboardAdapter;
import me.emmy.alley.spawn.SpawnHandler;
import me.emmy.alley.utils.assemble.Assemble;
import me.emmy.alley.utils.assemble.AssembleStyle;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.CommandFramework;
import me.emmy.alley.utils.menu.MenuListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Getter
@Setter
public class Alley extends JavaPlugin {

    @Getter
    private static Alley instance;

    private KitSettingRepository kitSettingRepository;
    private ProfileRepository profileRepository;
    private ScoreboardHandler scoreboardHandler;
    private ArenaRepository arenaRepository;
    private MatchRepository matchRepository;
    private QueueRepository queueRepository;
    private PartyRepository partyRepository;
    private KitRepository kitRepository;
    private HotbarUtility hotbarUtility;
    private ConfigHandler configHandler;
    private CommandFramework framework;
    private SpawnHandler spawnHandler;
    private MongoService mongoService;

    private String prefix = "§f[§dAlley§f] §r";

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        checkDescription();
        registerHandlers();
        registerDatabase();
        registerManagers();
        registerListeners();
        registerCommands();
        loadScoreboard();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.on(timeTaken);
    }

    @Override
    public void onDisable() {
        kitRepository.saveKits();
        CC.off();
    }

    private void checkDescription() {
        List<String> authors = getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emma", "Remi");

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Expected authors: &a" + expectedAuthors + "&f, Retrieved authors: &c" + authors));

        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&4&lAuthor mismatch! Shutting down the server."));
            System.exit(0);
            Bukkit.shutdown();
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "&aNo changes detected!"));
        }
    }

    private void registerHandlers() {
        configHandler = new ConfigHandler();
        scoreboardHandler = new ScoreboardHandler();
    }

    private void registerDatabase() {
        FileConfiguration config = configHandler.getConfigByName("database/database.yml");
        String uri = config.getString("mongo.uri");

        this.profileRepository = new ProfileRepository();
        this.profileRepository.setIProfile(new MongoProfileImpl());
        this.mongoService = new MongoService(uri);
    }

    private void registerManagers() {
        this.framework = new CommandFramework(this);
        this.hotbarUtility = new HotbarUtility();

        this.profileRepository.loadProfiles();

        this.queueRepository = new QueueRepository();
        this.queueRepository.initialize();

        this.matchRepository = new MatchRepository();
        this.kitSettingRepository = new KitSettingRepository();
        this.partyRepository = new PartyRepository();
        this.kitRepository = new KitRepository();
        this.kitRepository.loadKits();

        this.arenaRepository = new ArenaRepository();
        this.arenaRepository.loadArenas();

        this.spawnHandler = new SpawnHandler();
        this.spawnHandler.loadSpawnLocation();

    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ProfileListener(), this);
        getServer().getPluginManager().registerEvents(new HotbarListener(), this);
        getServer().getPluginManager().registerEvents(new PartyListener(), this);
        getServer().getPluginManager().registerEvents(new MatchListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaListener(), this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }

    private void registerCommands() {
        new AlleyCommand();

        //admin commands
        new SpawnItemsCommand();
        new SetSpawnCommand();
        new SpawnCommand();

        new PlaytimeCommand();

        new KitCommand();
        new KitSaveCommand();
        new KitSaveAllCommand();
        new KitCreateCommand();
        new KitDeleteCommand();
        new KitListCommand();
        new KitGetInvCommand();
        new KitSetInvCommand();
        new KitSetDescriptionCommand();
        new KitSetEditorSlotCommand();
        new KitSetUnrankedSlotCommand();
        new KitSetRankedSlotCommand();
        new KitSettingCommand();

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

        new PartyCommand();
        new PartyCreateCommand();
        new PartyLeaveCommand();
        new PartyInfoCommand();
        new PartyChatCommand();

        new UnrankedCommand();
        new RankedCommand();
        new SettingsCommand();
        new LeaderboardCommand();
        new StatsCommand();
        new SpectateCommand();
        new LeaveQueueCommand();
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
