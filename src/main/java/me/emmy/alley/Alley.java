package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.api.assemble.Assemble;
import me.emmy.alley.api.assemble.AssembleStyle;
import me.emmy.alley.api.command.CommandFramework;
import me.emmy.alley.api.menu.MenuListener;
import me.emmy.alley.arena.ArenaRepository;
import me.emmy.alley.arena.command.ArenaCommand;
import me.emmy.alley.arena.listener.ArenaListener;
import me.emmy.alley.command.AlleyCommand;
import me.emmy.alley.command.AlleyReloadCommand;
import me.emmy.alley.command.admin.debug.FFAStateCommand;
import me.emmy.alley.command.admin.debug.StateCommand;
import me.emmy.alley.essential.command.EnchantCommand;
import me.emmy.alley.essential.command.RenameCommand;
import me.emmy.alley.command.admin.management.PlaytimeCommand;
import me.emmy.alley.command.donator.HostCommand;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.database.MongoService;
import me.emmy.alley.essential.command.InvSeeCommand;
import me.emmy.alley.essential.command.troll.FakeExplosionCommand;
import me.emmy.alley.essential.command.troll.LaunchCommand;
import me.emmy.alley.essential.command.troll.PushCommand;
import me.emmy.alley.essential.command.troll.StrikeCommand;
import me.emmy.alley.essential.spawn.SpawnHandler;
import me.emmy.alley.essential.spawn.command.SetSpawnCommand;
import me.emmy.alley.essential.spawn.command.SpawnCommand;
import me.emmy.alley.essential.spawn.command.SpawnItemsCommand;
import me.emmy.alley.essential.spawn.listener.SpawnListener;
import me.emmy.alley.game.event.EventRepository;
import me.emmy.alley.game.event.command.EventCommand;
import me.emmy.alley.game.ffa.FFARepository;
import me.emmy.alley.game.ffa.combat.CombatManager;
import me.emmy.alley.game.ffa.command.admin.FFACommand;
import me.emmy.alley.game.ffa.listener.FFAListener;
import me.emmy.alley.game.ffa.safezone.FFASpawnHandler;
import me.emmy.alley.game.ffa.safezone.task.FFASpawnTask;
import me.emmy.alley.game.match.AbstractMatch;
import me.emmy.alley.game.match.MatchRepository;
import me.emmy.alley.game.match.command.admin.MatchCommand;
import me.emmy.alley.game.match.command.admin.impl.MatchInfoCommand;
import me.emmy.alley.game.match.command.player.CurrentMatchesCommand;
import me.emmy.alley.game.match.command.player.LeaveMatchCommand;
import me.emmy.alley.game.match.command.player.LeaveSpectatorCommand;
import me.emmy.alley.game.match.command.player.SpectateCommand;
import me.emmy.alley.game.match.listener.MatchListener;
import me.emmy.alley.game.match.snapshot.SnapshotRepository;
import me.emmy.alley.game.match.snapshot.command.InventoryCommand;
import me.emmy.alley.game.tournament.command.TournamentCommand;
import me.emmy.alley.hotbar.HotbarRepository;
import me.emmy.alley.hotbar.listener.HotbarListener;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.command.KitCommand;
import me.emmy.alley.kit.editor.command.KitEditorCommand;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.party.command.PartyCommand;
import me.emmy.alley.party.listener.PartyListener;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.command.ChallengesCommand;
import me.emmy.alley.profile.command.MatchHistoryCommand;
import me.emmy.alley.profile.command.ProfileMenuCommand;
import me.emmy.alley.profile.command.ThemesCommand;
import me.emmy.alley.profile.cosmetic.command.CosmeticCommand;
import me.emmy.alley.profile.cosmetic.repository.CosmeticRepository;
import me.emmy.alley.profile.division.DivisionRepository;
import me.emmy.alley.profile.division.command.DivisionCommand;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.profile.listener.ProfileListener;
import me.emmy.alley.profile.settings.command.MatchSettingsCommand;
import me.emmy.alley.profile.settings.command.PracticeSettingsCommand;
import me.emmy.alley.profile.settings.command.toggle.TogglePartyInvitesCommand;
import me.emmy.alley.profile.settings.command.toggle.TogglePartyMessagesCommand;
import me.emmy.alley.profile.settings.command.toggle.ToggleScoreboardCommand;
import me.emmy.alley.profile.settings.command.toggle.ToggleTablistCommand;
import me.emmy.alley.profile.settings.command.worldtime.*;
import me.emmy.alley.profile.shop.command.ShopCommand;
import me.emmy.alley.profile.shop.command.admin.SetCoinsCommand;
import me.emmy.alley.queue.QueueRepository;
import me.emmy.alley.queue.command.admin.QueueCommand;
import me.emmy.alley.queue.command.player.LeaveQueueCommand;
import me.emmy.alley.queue.command.player.QueuesCommand;
import me.emmy.alley.queue.command.player.RankedCommand;
import me.emmy.alley.queue.command.player.UnrankedCommand;
import me.emmy.alley.util.ServerUtil;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.chat.Logger;
import me.emmy.alley.visual.leaderboard.command.LeaderboardCommand;
import me.emmy.alley.visual.scoreboard.ScoreboardVisualizer;
import me.emmy.alley.visual.scoreboard.animation.ScoreboardTitleHandler;
import me.emmy.alley.visual.tablist.task.TablistUpdateTask;
import me.emmy.alley.world.WorldListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
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

    private CommandFramework commandFramework;
    private CosmeticRepository cosmeticRepository;
    private ProfileRepository profileRepository;
    private DivisionRepository divisionRepository;
    private FFASpawnHandler ffaSpawnHandler;
    private MongoService mongoService;
    private ArenaRepository arenaRepository;
    private QueueRepository queueRepository;
    private ConfigHandler configHandler;
    private MatchRepository matchRepository;
    private PartyRepository partyRepository;
    private CooldownRepository cooldownRepository;
    private CombatManager combatManager;
    private KitRepository kitRepository;
    private ScoreboardTitleHandler sbTitleHandler;
    private PartyRequest partyRequest;
    private KitSettingRepository kitSettingRepository;
    private SnapshotRepository snapshotRepository;
    private FFARepository ffaRepository;
    private SpawnHandler spawnHandler;
    private HotbarRepository hotbarRepository;
    private EventRepository eventRepository;

    private String prefix = "§f[§bAlley§f] §r";

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
        loadTablist();
        loadTasks();
        ServerUtil.setupWorld();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.pluginEnabled(timeTaken);
    }

    @Override
    public void onDisable() {
        profileRepository.getProfiles().forEach((uuid, profile) -> profile.save());

        ServerUtil.disconnectPlayers();
        kitRepository.saveKits();
        ffaRepository.saveFFAMatches();
        arenaRepository.saveArenas();

        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entity.remove();
            }
        }));

        CC.pluginDisabled();
    }

    private void checkDescription() {
        List<String> authors = getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Expected authors: &a" + expectedAuthors + "&f, Retrieved authors: &c" + authors));

        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
            Bukkit.shutdown();
        }
    }

    private void registerHandlers() {
        configHandler = new ConfigHandler();
        sbTitleHandler = new ScoreboardTitleHandler();
    }

    private String registerDatabase() {
        FileConfiguration config = configHandler.getConfig("database/database.yml");
        return config.getString("mongo.uri");
    }

    private void registerManagers() {
        Logger.logTime("CommandFramework", () -> this.commandFramework = new CommandFramework(this));
        Logger.logTime("QueueRepository", () -> this.queueRepository = new QueueRepository());
        Logger.logTime("KitSettingRepository", () -> this.kitSettingRepository = new KitSettingRepository());
        Logger.logTime("KitRepository", () -> this.kitRepository = new KitRepository());
        Logger.logTime("ArenaRepository", () -> this.arenaRepository = new ArenaRepository());
        Logger.logTime("FFARepository", () -> this.ffaRepository = new FFARepository());
        Logger.logTime("CosmeticRepository", () -> this.cosmeticRepository = new CosmeticRepository());
        Logger.logTime("ProfileRepository", () -> this.profileRepository = new ProfileRepository());
        Logger.logTime("DivisionRepository", () -> this.divisionRepository = new DivisionRepository());
        Logger.logTime("MongoService", () -> this.mongoService = new MongoService(registerDatabase()));
        Logger.logTime("HotbarRepository", () -> this.hotbarRepository = new HotbarRepository());
        Logger.logTime("Profiles", () -> this.profileRepository.loadProfiles());
        Logger.logTime("CooldownRepository", () -> this.cooldownRepository = new CooldownRepository());
        Logger.logTime("SnapshotRepository", () -> this.snapshotRepository = new SnapshotRepository());
        Logger.logTime("MatchRepository", () -> this.matchRepository = new MatchRepository());
        Logger.logTime("PartyRepository", () -> this.partyRepository = new PartyRepository());
        Logger.logTime("SpawnHandler", () -> this.spawnHandler = new SpawnHandler());
        Logger.logTime("CombatManager", () -> this.combatManager = new CombatManager());
        Logger.logTime("FFASpawnHandler", () -> this.ffaSpawnHandler = new FFASpawnHandler());
        Logger.logTime("EventRepository", () -> this.eventRepository = new EventRepository());
    }

    private void registerListeners() {
        Arrays.asList(
                new ProfileListener(),
                new HotbarListener(),
                new PartyListener(),
                new MatchListener(),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),
                new FFAListener(),
                new WorldListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        Logger.logTime("Admin Commands", () -> {
            new AlleyCommand();
            new AlleyReloadCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new QueueCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();

            //debugging
            new StateCommand();
            new FFAStateCommand();
            new MatchInfoCommand();

            //essential
            new InvSeeCommand();
            new RenameCommand();
            new EnchantCommand();
            new PlaytimeCommand();
            new SpawnItemsCommand();
            new SetSpawnCommand();
            new SpawnCommand();
            new SetCoinsCommand();

            //troll
            new FakeExplosionCommand();
            new LaunchCommand();
            new PushCommand();
            new StrikeCommand();
        });

        Logger.logTime("Donator Command", () -> {
            new HostCommand();
            new EventCommand();
            new TournamentCommand();
        });

        Logger.logTime("Player Commands", () -> {
            new DayCommand();
            new NightCommand();
            new SunsetCommand();
            new ResetTimeCommand();
            new TogglePartyInvitesCommand();
            new TogglePartyMessagesCommand();
            new ToggleScoreboardCommand();
            new ToggleTablistCommand();
            new ToggleWorldTimeCommand();

            new PartyCommand();

            new UnrankedCommand();
            new RankedCommand();
            new PracticeSettingsCommand();
            new LeaderboardCommand();
            new SpectateCommand();
            new LeaveSpectatorCommand();
            new LeaveMatchCommand();
            new CurrentMatchesCommand();
            new LeaveQueueCommand();
            new QueuesCommand();
            new MatchSettingsCommand();

            new InventoryCommand();

            new ShopCommand();
            new ChallengesCommand();
            new ProfileMenuCommand();
            new MatchHistoryCommand();
            new ThemesCommand();
            new KitEditorCommand();
        });
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardVisualizer());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void loadTablist() {
        if (configHandler.getTablistConfig().getBoolean("tablist.enabled")) {
            new TablistUpdateTask().runTaskTimer(this, 0L, 20L);
        }
    }

    private void loadTasks() {
        new FFASpawnTask(this.ffaSpawnHandler.getCuboid(), this).runTaskTimer(this, 0, 20);
    }

    /**
     * Get the configuration file by name
     *
     * @param fileName the name of the file
     * @return the file configuration
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the exact bukkit version
     *
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact() {
        String version = Bukkit.getServer().getVersion();
        version = version.split("MC: ")[1];
        version = version.split("\\)")[0];
        return version;
    }

    /**
     * Get the player count of a specific queue type (Unranked, FFA, Ranked)
     *
     * @param queue the queue
     * @return the player count
     */
    public int getPlayerCountOfGameType(String queue) {
        switch (queue) {
            case "Unranked":
                return (int) matchRepository.getMatches().stream()
                        .filter(match -> !match.isRanked())
                        .distinct()
                        .count() * 2; //* 2 because there are 2 players in a regular match, so we double the amount of matches
            case "Ranked":
                return (int) matchRepository.getMatches().stream()
                        .filter(AbstractMatch::isRanked)
                        .distinct()
                        .count() * 2; //same applies here
            case "FFA":
                return (int) profileRepository.getProfiles().values().stream()
                        .filter(profile -> profile.getState().equals(EnumProfileState.FFA))
                        .count(); //not needed, because we can just get every profile that is in the FFA state
            case "Bots":
                return 0;
        }
        return 0;
    }
}
