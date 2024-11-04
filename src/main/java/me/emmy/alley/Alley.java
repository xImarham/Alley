package me.emmy.alley;

import lombok.Getter;
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
import me.emmy.alley.command.admin.management.PlaytimeCommand;
import me.emmy.alley.command.donator.HostCommand;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.database.MongoService;
import me.emmy.alley.essential.command.*;
import me.emmy.alley.essential.command.troll.*;
import me.emmy.alley.essential.spawn.SpawnService;
import me.emmy.alley.essential.spawn.command.SetSpawnCommand;
import me.emmy.alley.essential.spawn.command.SpawnCommand;
import me.emmy.alley.essential.spawn.command.SpawnItemsCommand;
import me.emmy.alley.essential.spawn.listener.SpawnListener;
import me.emmy.alley.game.duel.DuelRepository;
import me.emmy.alley.game.duel.command.AcceptCommand;
import me.emmy.alley.game.duel.command.DuelCommand;
import me.emmy.alley.game.duel.command.DuelRequestsCommand;
import me.emmy.alley.game.duel.task.DuelRequestExpiryTask;
import me.emmy.alley.game.event.EventRepository;
import me.emmy.alley.game.event.command.EventCommand;
import me.emmy.alley.game.ffa.FFARepository;
import me.emmy.alley.game.ffa.combat.CombatEventManager;
import me.emmy.alley.game.ffa.command.admin.FFACommand;
import me.emmy.alley.game.ffa.listener.FFAListener;
import me.emmy.alley.game.ffa.spawn.FFACuboidService;
import me.emmy.alley.game.ffa.spawn.FFASpawnTask;
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
import me.emmy.alley.party.command.PartyCommand;
import me.emmy.alley.party.listener.PartyListener;
import me.emmy.alley.party.task.PartyRequestExpiryTask;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.command.*;
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
import me.emmy.alley.util.logger.Logger;
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
public class Alley extends JavaPlugin {

    @Getter private static Alley instance;

    private CommandFramework commandFramework;
    private CosmeticRepository cosmeticRepository;
    private ProfileRepository profileRepository;
    private DivisionRepository divisionRepository;
    private FFACuboidService ffaCuboidService;
    private MongoService mongoService;
    private ArenaRepository arenaRepository;
    private QueueRepository queueRepository;
    private ConfigHandler configHandler;
    private MatchRepository matchRepository;
    private PartyRepository partyRepository;
    private CooldownRepository cooldownRepository;
    private CombatEventManager combatEventManager;
    private KitRepository kitRepository;
    private ScoreboardTitleHandler scoreboardTitleHandler;
    private KitSettingRepository kitSettingRepository;
    private SnapshotRepository snapshotRepository;
    private FFARepository ffaRepository;
    private SpawnService spawnService;
    private HotbarRepository hotbarRepository;
    private EventRepository eventRepository;
    private DuelRepository duelRepository;

    private final String prefix = "§f[§b" + this.getDescription().getName() + "§f] §r";

    @Override
    public void onEnable() {
        instance = this;

        long start = System.currentTimeMillis();

        this.checkDescription();
        this.registerHandlers();
        this.registerManagers();
        this.registerListeners();
        this.registerCommands();
        this.loadScoreboard();
        this.loadTablist();
        this.runTasks();

        ServerUtil.setupWorld();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        Logger.pluginEnabled(timeTaken);
    }

    @Override
    public void onDisable() {
        this.profileRepository.getProfiles().forEach((uuid, profile) -> profile.save());

        ServerUtil.disconnectPlayers();

        this.kitRepository.saveKits();
        this.ffaRepository.saveFFAMatches();
        this.arenaRepository.saveArenas();

        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(entity -> {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entity.remove();
            }
        }));

        Logger.pluginDisabled();
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
        this.configHandler = new ConfigHandler();
        this.scoreboardTitleHandler = new ScoreboardTitleHandler();
    }

    private void registerManagers() {
        Logger.logTime(false, "CommandFramework", () -> this.commandFramework = new CommandFramework(this));
        Logger.logTime(false, "QueueRepository", () -> this.queueRepository = new QueueRepository());
        Logger.logTime(false, "KitSettingRepository", () -> this.kitSettingRepository = new KitSettingRepository());
        Logger.logTime(false, "KitRepository", () -> this.kitRepository = new KitRepository());
        Logger.logTime(false, "ArenaRepository", () -> this.arenaRepository = new ArenaRepository());
        Logger.logTime(false, "FFARepository", () -> this.ffaRepository = new FFARepository());
        Logger.logTime(false, "CosmeticRepository", () -> this.cosmeticRepository = new CosmeticRepository());
        Logger.logTime(false, "ProfileRepository", () -> this.profileRepository = new ProfileRepository());
        Logger.logTime(false, "DivisionRepository", () -> this.divisionRepository = new DivisionRepository());
        Logger.logTime(false, "MongoService", () -> this.mongoService = new MongoService(this.configHandler.getDatabaseConfig().getString("mongo.uri")));
        Logger.logTime(false, "HotbarRepository", () -> this.hotbarRepository = new HotbarRepository());
        Logger.logTime(false, "Profiles", () -> this.profileRepository.loadProfiles());
        Logger.logTime(false, "CooldownRepository", () -> this.cooldownRepository = new CooldownRepository());
        Logger.logTime(false, "SnapshotRepository", () -> this.snapshotRepository = new SnapshotRepository());
        Logger.logTime(false, "MatchRepository", () -> this.matchRepository = new MatchRepository());
        Logger.logTime(false, "PartyRepository", () -> this.partyRepository = new PartyRepository());
        Logger.logTime(false, "SpawnService", () -> this.spawnService = new SpawnService());
        Logger.logTime(false, "CombatEventManager", () -> this.combatEventManager = new CombatEventManager());
        Logger.logTime(false, "FFACuboidService", () -> this.ffaCuboidService = new FFACuboidService());
        Logger.logTime(false, "EventRepository", () -> this.eventRepository = new EventRepository());
        Logger.logTime(false, "DuelRepository", () -> this.duelRepository = new DuelRepository());
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
        Logger.logTime(false, "Admin Commands", () -> {
            new AlleyCommand();
            new AlleyReloadCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new QueueCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();

            new StateCommand();
            new FFAStateCommand();
            new MatchInfoCommand();

            new EnchantCommand();
            new InvSeeCommand();
            new MoreCommand();
            new PotionDurationCommand();
            new RefillCommand();
            new RemoveEnchantsCommand();
            new RenameCommand();
            new PlaytimeCommand();
            new SpawnItemsCommand();
            new SetSpawnCommand();
            new SpawnCommand();
            new SetCoinsCommand();

            new FakeExplosionCommand();
            new HeartAttackCommand();
            new LaunchCommand();
            new PushCommand();
            new StrikeCommand();
            new TrollCommand();
        });

        Logger.logTime(false, "Donator Command", () -> {
            new HostCommand();
            new EventCommand();
            new TournamentCommand();
        });

        Logger.logTime(false, "Player Commands", () -> {
            new ChatCommand();
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
            new AcceptCommand();
            new DuelCommand();
            new DuelRequestsCommand();
            new InventoryCommand();
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
        if (this.configHandler.getTablistConfig().getBoolean("tablist.enabled")) {
            new TablistUpdateTask().runTaskTimer(this, 0L, 20L);
        }
    }

    private void runTasks() {
        Logger.logTime(true, "FFASpawnTask", () -> {
            FFASpawnTask ffaSpawnTask = new FFASpawnTask(this.ffaCuboidService.getCuboid(), this);
            ffaSpawnTask.runTaskTimerAsynchronously(this, 0L, 5L);
        });

        Logger.logTime(true, "PartyRequestExpiryTask", () -> {
            PartyRequestExpiryTask partyRequestExpiryTask = new PartyRequestExpiryTask();
            partyRequestExpiryTask.runTaskTimerAsynchronously(this, 40L, 40L);
        });

        Logger.logTime(true, "DuelRequestExpiryTask", () -> {
            DuelRequestExpiryTask duelRequestExpiryTask = new DuelRequestExpiryTask();
            duelRequestExpiryTask.runTaskTimerAsynchronously(this, 40L, 40L);
        });
    }

    /**
     * Get the configuration file by name
     *
     * @param fileName the name of the file
     * @return the file configuration
     */
    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(this.getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Get the exact bukkit version
     *
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact() {
        String version = this.getServer().getVersion();
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
                return (int) this.profileRepository.getProfiles().values().stream()
                        .filter(profile -> profile.getState().equals(EnumProfileState.PLAYING))
                        .filter(profile -> !profile.getMatch().isRanked())
                        .count();
            case "Ranked":
                return (int) this.profileRepository.getProfiles().values().stream()
                        .filter(profile -> profile.getState().equals(EnumProfileState.PLAYING))
                        .filter(profile -> profile.getMatch().isRanked())
                        .count();
            case "FFA":
                return (int) this.profileRepository.getProfiles().values().stream()
                        .filter(profile -> profile.getState().equals(EnumProfileState.FFA))
                        .count();
            case "Bots":
                return 0;
        }
        return 0;
    }
}
