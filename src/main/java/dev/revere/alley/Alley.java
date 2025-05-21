package dev.revere.alley;

import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.command.CommandFramework;
import dev.revere.alley.api.constant.PluginConstant;
import dev.revere.alley.api.discord.DiscordBridge;
import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.api.server.ServerEnvironment;
import dev.revere.alley.command.CommandDataCollector;
import dev.revere.alley.command.CommandUtility;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.core.CoreAdapter;
import dev.revere.alley.core.listener.CoreChatListener;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.essential.chat.ChatListener;
import dev.revere.alley.essential.emoji.EmojiRepository;
import dev.revere.alley.essential.emoji.listener.EmojiListener;
import dev.revere.alley.essential.filter.ProfanityFilter;
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
import dev.revere.alley.feature.kit.setting.KitSettingService;
import dev.revere.alley.feature.layout.LayoutService;
import dev.revere.alley.feature.layout.listener.LayoutListener;
import dev.revere.alley.feature.leaderboard.LeaderboardService;
import dev.revere.alley.feature.level.LevelService;
import dev.revere.alley.feature.queue.QueueService;
import dev.revere.alley.feature.scoreboard.ScoreboardVisualizer;
import dev.revere.alley.feature.server.ServerService;
import dev.revere.alley.feature.spawn.SpawnService;
import dev.revere.alley.feature.spawn.listener.SpawnListener;
import dev.revere.alley.feature.tablist.task.TablistUpdateTask;
import dev.revere.alley.feature.title.TitleService;
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
import dev.revere.alley.papi.AlleyPlaceholderExpansion;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.listener.ProfileListener;
import dev.revere.alley.reflection.ReflectionRepository;
import dev.revere.alley.task.ArrowRemovalTask;
import dev.revere.alley.task.RepositoryCleanupTask;
import dev.revere.alley.tool.animation.AnimationRepository;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.logger.PluginLogger;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * Alley – A modern, modular Practice PvP core built from the ground up for Minecraft 1.8.
 * <p>
 * Developed by Revere Inc., Alley focuses on clean, professional, and readable code,
 * making it easy for developers to jump into practice PvP development with minimal friction.
 * </p>
 * <p>
 * Alley is open source under the terms described in the README:
 * <a href="https://github.com/RevereInc/alley-practice">GitHub Repository</a>
 * </p>
 *
 * @author Emmy, Remi
 * @version 2.0 — Complete recode (entirely rewritten from scratch)
 * @see <a href="https://revere.dev">revere.dev</a>
 * @see <a href="https://discord.gg/revere">Discord Support</a>
 * @since 19/04/2024
 */
@Getter
public class Alley extends JavaPlugin {

    @Getter
    private static Alley instance;

    private PluginConstant pluginConstant;
    private ServerEnvironment serverEnvironment;
    private Assemble assemble;
    private CommandFramework commandFramework;
    private CommandDataCollector commandDataCollector;
    private CoreAdapter coreAdapter;
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
    private AnimationRepository animationRepository;
    private ReflectionRepository reflectionRepository;
    private DiscordBridge discordBridge;
    private TitleService titleService;
    private LevelService levelService;
    //private ParkourService parkourService;
    private ProfanityFilter profanityFilter;
    private LayoutService layoutService;

    private boolean loaded;

    public Alley() {
        new AlleyAPI();
    }

    public void onEnable() {
        this.loaded = false;
        instance = this;

        long start = System.currentTimeMillis();

        this.checkDescription();
        this.initializeServices();
        this.registerExpansion();
        this.registerListeners();
        this.runTasks();
        this.initializeEssentials();

        CommandUtility.registerCommands();
        this.serverEnvironment.setupWorld();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        PluginLogger.onEnable(timeTaken);
        this.loaded = true;

        AlleyAPI.getInstance().runOnEnableCallbacks();
    }

    @Override
    public void onDisable() {
        this.profileService.getProfiles().forEach((uuid, profile) -> profile.save());
        this.matchRepository.endPresentMatches();

        this.assemble.interruptAndClose(true);

        this.serverEnvironment.disconnectPlayers();
        this.serverEnvironment.clearEntities(EntityType.DROPPED_ITEM);

        this.kitService.saveKits();
        this.arenaService.getArenas().forEach(AbstractArena::saveArena);
        this.divisionService.saveDivisions();

        PluginLogger.onDisable();

        AlleyAPI.getInstance().runOnDisableCallbacks();
    }

    private void checkDescription() {
        List<String> authors = this.getDescription().getAuthors();
        List<String> expectedAuthors = Arrays.asList("Emmy", "Remi");
        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
        }
    }

    private void initializeServices() {
        final Map<String, Runnable> services = new LinkedHashMap<>();

        services.put(PluginConstant.class.getSimpleName(), () -> this.pluginConstant = new PluginConstant(this, "dev.revere.alley", ChatColor.AQUA));
        services.put(ServerEnvironment.class.getSimpleName(), () -> this.serverEnvironment = new ServerEnvironment(this, false, false, false, false, true));
        services.put(ConfigService.class.getSimpleName(), () -> this.configService = new ConfigService());
        services.put(MongoService.class.getSimpleName(), () -> this.mongoService = new MongoService(this));
        services.put(CommandFramework.class.getSimpleName(), () -> this.commandFramework = new CommandFramework(this));
        services.put(CommandDataCollector.class.getSimpleName(), () -> this.commandDataCollector = new CommandDataCollector());
        services.put(CoreAdapter.class.getSimpleName(), () -> this.coreAdapter = new CoreAdapter(this));
        services.put(QueueService.class.getSimpleName(), () -> this.queueService = new QueueService(this));
        services.put(KitSettingService.class.getSimpleName(), () -> this.kitSettingService = new KitSettingService());
        services.put(KitService.class.getSimpleName(), () -> this.kitService = new KitService(this));
        services.put(ArenaService.class.getSimpleName(), () -> this.arenaService = new ArenaService(this));
        services.put(FFAService.class.getSimpleName(), () -> this.ffaService = new FFAService(this));
        services.put(CosmeticRepository.class.getSimpleName(), () -> this.cosmeticRepository = new CosmeticRepository());
        services.put(DivisionService.class.getSimpleName(), () -> this.divisionService = new DivisionService(this));
        services.put(LevelService.class.getSimpleName(), () -> this.levelService = new LevelService(this));
        services.put(TitleService.class.getSimpleName(), () -> this.titleService = new TitleService(this));

        services.put(ProfileService.class.getSimpleName(), () -> {
            this.profileService = new ProfileService(this);
            this.profileService.loadProfiles();
        });

        services.put(HotbarService.class.getSimpleName(), () -> this.hotbarService = new HotbarService(this));
        services.put(CooldownRepository.class.getSimpleName(), () -> this.cooldownRepository = new CooldownRepository());
        services.put(SnapshotRepository.class.getSimpleName(), () -> this.snapshotRepository = new SnapshotRepository());
        services.put(MatchRepository.class.getSimpleName(), () -> this.matchRepository = new MatchRepository(this));
        services.put(PartyService.class.getSimpleName(), () -> this.partyService = new PartyService(this));
        services.put(SpawnService.class.getSimpleName(), () -> this.spawnService = new SpawnService(this.configService));
        services.put(FFASpawnService.class.getSimpleName(), () -> this.ffaSpawnService = new FFASpawnService());
        services.put(DuelRequestService.class.getSimpleName(), () -> this.duelRequestService = new DuelRequestService(this));
        services.put(CombatService.class.getSimpleName(), () -> this.combatService = new CombatService(this));
        services.put(LeaderboardService.class.getSimpleName(), () -> this.leaderboardService = new LeaderboardService(this));
        services.put(EloCalculator.class.getSimpleName(), () -> this.eloCalculator = new EloCalculator());
        services.put(ServerService.class.getSimpleName(), () -> this.serverService = new ServerService());
        services.put(AnimationRepository.class.getSimpleName(), () -> this.animationRepository = new AnimationRepository(this));
        services.put(Assemble.class.getSimpleName() + " API", () -> this.assemble = new Assemble(this, new ScoreboardVisualizer(this)));
        services.put(ReflectionRepository.class.getSimpleName(), () -> this.reflectionRepository = new ReflectionRepository(this));
        //services.put(ParkourService.class.getSimpleName(), () -> this.parkourService = new ParkourService(this, this.configService.getSettingsConfig().getString("parkour.starter-location")));
        services.put(ProfanityFilter.class.getSimpleName(), () -> this.profanityFilter = new ProfanityFilter(this));
        services.put(LayoutService.class.getSimpleName(), () -> this.layoutService = new LayoutService(this));

        services.forEach(Logger::logTime);
    }

    private void registerExpansion() {
        if (this.getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            Logger.logTime(AlleyPlaceholderExpansion.class.getSimpleName(), () -> {
                AlleyPlaceholderExpansion expansion = new AlleyPlaceholderExpansion(this);
                expansion.register();
                Logger.log("Successfully registered PlaceholderAPI expansion.");
            });
        }
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
                new EmojiListener(this),
                new CombatListener(this),
                //new ParkourListener(this),
                new CoreChatListener(this),
                new LayoutListener(this)
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

    private void initializeEssentials() {
        FileConfiguration config = this.configService.getSettingsConfig();

        Logger.logTime("Essential Services", () -> {
            if (config.getBoolean("essentials.emojis")) {
                this.emojiRepository = new EmojiRepository();
                this.getServer().getPluginManager().registerEvents(new EmojiListener(this), this);
                Logger.log("Emojis have been enabled.");
            }


            String webhookUrl = config.getString("essentials.chat-logging.webhook-url");
            if (config.getBoolean("essentials.chat-logging.enabled")) {
                this.discordBridge = new DiscordBridge(this, webhookUrl);
                this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
                Logger.log("Chat logging is enabled. Discord Webhook URL: " + webhookUrl);
            }
        });
    }
}