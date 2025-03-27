package dev.revere.alley;

import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.command.CommandFramework;
import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.command.CommandUtility;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.essential.emoji.EmojiRepository;
import dev.revere.alley.essential.emoji.listener.EmojiListener;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.ArenaService;
import dev.revere.alley.feature.arena.listener.ArenaListener;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.combat.listener.CombatListener;
import dev.revere.alley.feature.cooldown.CooldownRepository;
import dev.revere.alley.feature.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.feature.division.DivisionService;
import dev.revere.alley.feature.elo.EloCalculator;
import dev.revere.alley.feature.hotbar.HotbarService;
import dev.revere.alley.feature.hotbar.listener.HotbarListener;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.feature.kit.settings.KitSettingService;
import dev.revere.alley.feature.leaderboard.LeaderboardService;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.scoreboard.ScoreboardTitleAnimator;
import dev.revere.alley.feature.scoreboard.ScoreboardVisualizer;
import dev.revere.alley.feature.service.ServerService;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.feature.spawn.listener.SpawnListener;
import dev.revere.alley.feature.tablist.task.TablistUpdateTask;
import dev.revere.alley.feature.world.WorldListener;
import dev.revere.alley.game.duel.DuelRequestService;
import dev.revere.alley.game.ffa.FFAService;
import dev.revere.alley.game.ffa.cuboid.FFASpawnService;
import dev.revere.alley.game.ffa.listener.FFAListener;
import dev.revere.alley.game.ffa.listener.impl.FFACuboidListener;
import dev.revere.alley.game.match.MatchRepository;
import dev.revere.alley.game.match.listener.MatchListener;
import dev.revere.alley.game.match.listener.impl.MatchBlockListener;
import dev.revere.alley.game.match.listener.impl.MatchDamageListener;
import dev.revere.alley.game.match.listener.impl.MatchDisconnectListener;
import dev.revere.alley.game.match.listener.impl.MatchInteractListener;
import dev.revere.alley.game.match.snapshot.SnapshotRepository;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.game.party.listener.PartyListener;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.listener.ProfileListener;
import dev.revere.alley.task.ArrowRemovalTask;
import dev.revere.alley.task.RepositoryCleanupTask;
import dev.revere.alley.util.ServerUtil;
import dev.revere.alley.util.logger.Logger;
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
    private ProfileService profileService;
    private DivisionService divisionService;
    private FFASpawnService ffaSpawnService;
    private MongoService mongoService;
    private ArenaService arenaService;
    private QueueService queueService;
    private ConfigService configService;
    private MatchRepository matchRepository;
    private PartyService partyService;
    private CooldownRepository cooldownRepository;
    private KitService kitService;
    private ScoreboardTitleAnimator scoreboardTitleAnimator;
    private KitSettingService kitSettingService;
    private SnapshotRepository snapshotRepository;
    private FFAService ffaService;
    private SpawnService spawnService;
    private HotbarService hotbarService;
    private DuelRequestService duelRequestService;
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
        this.initializeServices();
        this.registerListeners();
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
        this.profileService.getProfiles().forEach((uuid, profile) -> profile.save());
        this.matchRepository.endPresentMatches();

        this.assemble.interruptAndClose(true);

        ServerUtil.disconnectPlayers();
        ServerUtil.clearEntities(EntityType.DROPPED_ITEM);

        this.kitService.saveKits();
        this.ffaService.saveFFAMatches();
        this.arenaService.getArenas().forEach(AbstractArena::saveArena);
        this.divisionService.saveDivisions();

        Logger.pluginDisabled();
    }

    private void checkDescription() {
        List<String> authors = this.getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");
        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
        }
    }

    private void initializeServices() {
        final Map<String, Runnable> managers = new LinkedHashMap<>();

        managers.put(ConfigService.class.getSimpleName(), () -> this.configService = new ConfigService());
        managers.put(MongoService.class.getSimpleName(), () -> this.mongoService = new MongoService(this.configService.getDatabaseConfig().getString("mongo.uri"), this.configService.getDatabaseConfig().getString("mongo.database")));
        managers.put(CommandFramework.class.getSimpleName(), () -> this.commandFramework = new CommandFramework(this));
        managers.put(QueueService.class.getSimpleName(), () -> this.queueService = new QueueService(this));
        managers.put(KitSettingService.class.getSimpleName(), () -> this.kitSettingService = new KitSettingService());
        managers.put(KitService.class.getSimpleName(), () -> this.kitService = new KitService(this));
        managers.put(ArenaService.class.getSimpleName(), () -> this.arenaService = new ArenaService());
        managers.put(FFAService.class.getSimpleName(), () -> this.ffaService = new FFAService());
        managers.put(CosmeticRepository.class.getSimpleName(), () -> this.cosmeticRepository = new CosmeticRepository());
        managers.put(DivisionService.class.getSimpleName(), () -> this.divisionService = new DivisionService(this));
        managers.put(ProfileService.class.getSimpleName(), () -> { this.profileService = new ProfileService(); this.profileService.loadProfiles(); });
        managers.put(HotbarService.class.getSimpleName(), () -> this.hotbarService = new HotbarService());
        managers.put(CooldownRepository.class.getSimpleName(), () -> this.cooldownRepository = new CooldownRepository());
        managers.put(SnapshotRepository.class.getSimpleName(), () -> this.snapshotRepository = new SnapshotRepository());
        managers.put(MatchRepository.class.getSimpleName(), () -> this.matchRepository = new MatchRepository());
        managers.put(PartyService.class.getSimpleName(), () -> this.partyService = new PartyService());
        managers.put(SpawnService.class.getSimpleName(), () -> this.spawnService = new SpawnService(this.configService));
        managers.put(FFASpawnService.class.getSimpleName(), () -> this.ffaSpawnService = new FFASpawnService());
        managers.put(DuelRequestService.class.getSimpleName(), () -> this.duelRequestService = new DuelRequestService());
        managers.put(EmojiRepository.class.getSimpleName(), () -> this.emojiRepository = new EmojiRepository());
        managers.put(CombatService.class.getSimpleName(), () -> this.combatService = new CombatService());
        managers.put(LeaderboardService.class.getSimpleName(), () -> this.leaderboardService = new LeaderboardService());
        managers.put(EloCalculator.class.getSimpleName(), () -> this.eloCalculator = new EloCalculator());
        managers.put(ServerService.class.getSimpleName(), () -> this.serverService = new ServerService());
        managers.put(ScoreboardTitleAnimator.class.getSimpleName(), () -> this.scoreboardTitleAnimator = new ScoreboardTitleAnimator(this.configService.getScoreboardConfig()));
        managers.put(Assemble.class.getSimpleName() + " API", () -> this.assemble = new Assemble(this, new ScoreboardVisualizer()));

        managers.forEach(Logger::logTime);
    }

    private void registerListeners() {
        Arrays.asList(
                new ProfileListener(this.profileService),
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
                new FFACuboidListener(this.ffaSpawnService.getCuboid(), this),
                new WorldListener(),
                new EmojiListener(),
                new CombatListener(this)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void runTasks() {
        final Map<String, Runnable> runnables = new LinkedHashMap<>();

        runnables.put(RepositoryCleanupTask.class.getSimpleName(), () -> new RepositoryCleanupTask().runTaskTimer(this, 0L, 40L));
        runnables.put(ArrowRemovalTask.class.getSimpleName(), () -> new ArrowRemovalTask().runTaskTimer(this, 20L, 20L));

        if (this.configService.getTablistConfig().getBoolean("tablist.enabled")) {
            runnables.put(TablistUpdateTask.class.getSimpleName(), () -> new TablistUpdateTask().runTaskTimer(this, 0L, 20L));
        }

        runnables.forEach(Logger::logTimeTask);
    }
}