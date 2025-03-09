package dev.revere.alley;

import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.assemble.enums.EnumAssembleStyle;
import dev.revere.alley.api.command.CommandFramework;
import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.command.CommandUtility;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.essential.emoji.EmojiRepository;
import dev.revere.alley.essential.emoji.listener.EmojiListener;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.ArenaRepository;
import dev.revere.alley.feature.arena.listener.ArenaListener;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.cooldown.CooldownRepository;
import dev.revere.alley.feature.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.feature.division.DivisionRepository;
import dev.revere.alley.feature.elo.EloCalculator;
import dev.revere.alley.feature.hotbar.HotbarRepository;
import dev.revere.alley.feature.hotbar.listener.HotbarListener;
import dev.revere.alley.feature.kit.KitRepository;
import dev.revere.alley.feature.kit.settings.KitSettingRepository;
import dev.revere.alley.feature.leaderboard.LeaderboardService;
import dev.revere.alley.feature.queue.QueueRepository;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.feature.spawn.listener.SpawnListener;
import dev.revere.alley.game.duel.DuelRequestHandler;
import dev.revere.alley.game.ffa.FFARepository;
import dev.revere.alley.game.ffa.cuboid.FFACuboidServiceImpl;
import dev.revere.alley.game.ffa.listener.FFAListener;
import dev.revere.alley.game.ffa.listener.impl.FFACuboidListener;
import dev.revere.alley.game.match.MatchRepository;
import dev.revere.alley.game.match.listener.MatchListener;
import dev.revere.alley.game.match.listener.impl.MatchBlockListener;
import dev.revere.alley.game.match.listener.impl.MatchDamageListener;
import dev.revere.alley.game.match.listener.impl.MatchDisconnectListener;
import dev.revere.alley.game.match.listener.impl.MatchInteractListener;
import dev.revere.alley.game.match.snapshot.SnapshotRepository;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.game.party.listener.PartyListener;
import dev.revere.alley.profile.ProfileRepository;
import dev.revere.alley.profile.listener.ProfileListener;
import dev.revere.alley.service.ServerService;
import dev.revere.alley.task.RepositoryCleanupTask;
import dev.revere.alley.util.ServerUtil;
import dev.revere.alley.util.logger.Logger;
import dev.revere.alley.visual.scoreboard.ScoreboardVisualizer;
import dev.revere.alley.visual.scoreboard.animation.ScoreboardTitleHandler;
import dev.revere.alley.visual.tablist.task.TablistUpdateTask;
import dev.revere.alley.world.WorldListener;
import lombok.Getter;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class Alley extends JavaPlugin {

    @Getter private static Alley instance;

    private Assemble assemble;
    private CommandFramework commandFramework;
    private CosmeticRepository cosmeticRepository;
    private ProfileRepository profileRepository;
    private DivisionRepository divisionRepository;
    private FFACuboidServiceImpl ffaCuboidService;
    private MongoService mongoService;
    private ArenaRepository arenaRepository;
    private QueueRepository queueRepository;
    private ConfigService configService;
    private MatchRepository matchRepository;
    private PartyHandler partyHandler;
    private CooldownRepository cooldownRepository;
    private KitRepository kitRepository;
    private ScoreboardTitleHandler scoreboardTitleHandler;
    private KitSettingRepository kitSettingRepository;
    private SnapshotRepository snapshotRepository;
    private FFARepository ffaRepository;
    private SpawnService spawnService;
    private HotbarRepository hotbarRepository;
    private DuelRequestHandler duelRequestHandler;
    private EmojiRepository emojiRepository;
    private CombatService combatService;
    private LeaderboardService leaderboardService;
    private EloCalculator eloCalculator;
    private ServerService serverService;

    private boolean loaded;

    public void onEnable() {
        this.loaded = false;
        instance = this;

        long start = System.currentTimeMillis();

        this.checkDescription();
        this.initializeConfigService();
        this.initializeMongo();
        this.initializeManagers();
        this.registerListeners();
        this.initializeScoreboard();
        this.runTasks();

        CommandUtility.registerCommands();
        ServerUtil.setupWorld();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        Logger.pluginEnabled(timeTaken);
        this.loaded = true;
    }

    @Override
    public void onDisable() {
        this.profileRepository.getProfiles().forEach((uuid, profile) -> profile.save());
        //this.matchRepository.endPresentMatches();

        this.assemble.interruptAndClose(true);

        ServerUtil.disconnectPlayers();
        ServerUtil.clearEntities(EntityType.DROPPED_ITEM);

        this.kitRepository.saveKits();
        this.ffaRepository.saveFFAMatches();
        this.arenaRepository.getArenas().forEach(Arena::saveArena);
        this.divisionRepository.saveDivisions();

        Logger.pluginDisabled();
    }

    private void checkDescription() {
        List<String> authors = this.getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");
        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
        }
    }

    private void initializeConfigService() {
        this.configService = new ConfigService();
    }

    private void initializeMongo() {
        Logger.logTime("MongoService", () -> this.mongoService = new MongoService(
                this.configService.getDatabaseConfig().getString("mongo.uri"),
                this.configService.getDatabaseConfig().getString("mongo.database")
        ));
    }

    private void initializeManagers() {
        final Map<String, Runnable> managers = new LinkedHashMap<>();

        managers.put("CommandFramework", () -> this.commandFramework = new CommandFramework(this));
        managers.put("QueueRepository", () -> this.queueRepository = new QueueRepository(this));
        managers.put("KitSettingRepository", () -> this.kitSettingRepository = new KitSettingRepository());
        managers.put("KitRepository", () -> this.kitRepository = new KitRepository(this));
        managers.put("ArenaRepository", () -> this.arenaRepository = new ArenaRepository());
        managers.put("FFARepository", () -> this.ffaRepository = new FFARepository());
        managers.put("CosmeticRepository", () -> this.cosmeticRepository = new CosmeticRepository());
        managers.put("DivisionRepository", () -> this.divisionRepository = new DivisionRepository(this));
        managers.put("ProfileRepository", () -> { this.profileRepository = new ProfileRepository(); this.profileRepository.loadProfiles(); });
        managers.put("HotbarRepository", () -> this.hotbarRepository = new HotbarRepository());
        managers.put("CooldownRepository", () -> this.cooldownRepository = new CooldownRepository());
        managers.put("SnapshotRepository", () -> this.snapshotRepository = new SnapshotRepository());
        managers.put("MatchRepository", () -> this.matchRepository = new MatchRepository());
        managers.put("PartyHandler", () -> this.partyHandler = new PartyHandler());
        managers.put("SpawnService", () -> this.spawnService = new SpawnService(this.configService));
        managers.put("FFACuboidService", () -> this.ffaCuboidService = new FFACuboidServiceImpl());
        managers.put("DuelRequestHandler", () -> this.duelRequestHandler = new DuelRequestHandler());
        managers.put("EmojiRepository", () -> this.emojiRepository = new EmojiRepository());
        managers.put("CombatService", () -> this.combatService = new CombatService());
        managers.put("LeaderboardService", () -> this.leaderboardService = new LeaderboardService());
        managers.put("EloCalculator", () -> this.eloCalculator = new EloCalculator());
        managers.put("ServerService", () -> this.serverService = new ServerService());

        managers.forEach(Logger::logTime);
    }

    private void registerListeners() {
        Arrays.asList(
                new ProfileListener(this.profileRepository),
                new HotbarListener(),
                new PartyListener(),
                new MatchListener(this),
                new MatchInteractListener(this),
                new MatchDisconnectListener(this),
                new MatchDamageListener(this),
                new MatchBlockListener(this),
                new ArenaListener(),
                new MenuListener(),
                new SpawnListener(),
                new FFAListener(this),
                new FFACuboidListener(this.ffaCuboidService.getCuboid(), this),
                new WorldListener(),
                new EmojiListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void initializeScoreboard() {
        this.scoreboardTitleHandler = new ScoreboardTitleHandler(this.configService.getScoreboardConfig());

        this.assemble = new Assemble(this, new ScoreboardVisualizer());
        this.assemble.setTicks(2);
        this.assemble.setAssembleStyle(EnumAssembleStyle.MODERN);
    }

    private void runTasks() {
        final Map<String, Runnable> runnables = new LinkedHashMap<>();

        runnables.put("RepositoryCleanupTask", () -> {
            RepositoryCleanupTask repositoryCleanupTask = new RepositoryCleanupTask();
            repositoryCleanupTask.runTaskTimer(this, 0L, 40L);
        });

        if (this.configService.getTablistConfig().getBoolean("tablist.enabled")) {
            runnables.put("TablistUpdateTask", () -> {
                TablistUpdateTask tablistUpdateTask = new TablistUpdateTask();
                tablistUpdateTask.runTaskTimer(this, 0L, 20L);
            });
        }

        runnables.forEach(Logger::logTimeTask);
    }
}