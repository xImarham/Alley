package dev.revere.alley;

import lombok.Getter;
import dev.revere.alley.api.assemble.Assemble;
import dev.revere.alley.api.assemble.AssembleStyle;
import dev.revere.alley.api.command.CommandFramework;
import dev.revere.alley.api.menu.MenuListener;
import dev.revere.alley.arena.ArenaRepository;
import dev.revere.alley.arena.command.ArenaCommand;
import dev.revere.alley.arena.listener.ArenaListener;
import dev.revere.alley.command.AlleyCommand;
import dev.revere.alley.command.AlleyReloadCommand;
import dev.revere.alley.command.admin.debug.FFAStateCommand;
import dev.revere.alley.command.admin.debug.StateCommand;
import dev.revere.alley.command.admin.management.PlaytimeCommand;
import dev.revere.alley.command.donator.HostCommand;
import dev.revere.alley.config.ConfigHandler;
import dev.revere.alley.cooldown.CooldownRepository;
import dev.revere.alley.database.MongoService;
import dev.revere.alley.essential.command.*;
import dev.revere.alley.essential.command.troll.*;
import dev.revere.alley.essential.spawn.SpawnService;
import dev.revere.alley.essential.spawn.command.SetSpawnCommand;
import dev.revere.alley.essential.spawn.command.SpawnCommand;
import dev.revere.alley.essential.spawn.command.SpawnItemsCommand;
import dev.revere.alley.essential.spawn.listener.SpawnListener;
import dev.revere.alley.game.duel.DuelRepository;
import dev.revere.alley.game.duel.command.AcceptCommand;
import dev.revere.alley.game.duel.command.DuelCommand;
import dev.revere.alley.game.duel.command.DuelRequestsCommand;
import dev.revere.alley.game.duel.task.DuelRequestExpiryTask;
import dev.revere.alley.game.event.EventRepository;
import dev.revere.alley.game.event.command.EventCommand;
import dev.revere.alley.game.ffa.FFARepository;
import dev.revere.alley.game.ffa.combat.CombatEventManager;
import dev.revere.alley.game.ffa.command.admin.FFACommand;
import dev.revere.alley.game.ffa.listener.FFAListener;
import dev.revere.alley.game.ffa.spawn.FFACuboidService;
import dev.revere.alley.game.ffa.spawn.FFASpawnTask;
import dev.revere.alley.game.match.MatchRepository;
import dev.revere.alley.game.match.command.admin.MatchCommand;
import dev.revere.alley.game.match.command.admin.impl.MatchInfoCommand;
import dev.revere.alley.game.match.command.player.CurrentMatchesCommand;
import dev.revere.alley.game.match.command.player.LeaveMatchCommand;
import dev.revere.alley.game.match.command.player.LeaveSpectatorCommand;
import dev.revere.alley.game.match.command.player.SpectateCommand;
import dev.revere.alley.game.match.listener.MatchListener;
import dev.revere.alley.game.match.snapshot.SnapshotRepository;
import dev.revere.alley.game.match.snapshot.command.InventoryCommand;
import dev.revere.alley.game.tournament.command.TournamentCommand;
import dev.revere.alley.hotbar.HotbarRepository;
import dev.revere.alley.hotbar.listener.HotbarListener;
import dev.revere.alley.kit.KitRepository;
import dev.revere.alley.kit.command.KitCommand;
import dev.revere.alley.kit.editor.command.KitEditorCommand;
import dev.revere.alley.kit.settings.KitSettingRepository;
import dev.revere.alley.party.PartyRepository;
import dev.revere.alley.party.command.PartyCommand;
import dev.revere.alley.party.listener.PartyListener;
import dev.revere.alley.party.task.PartyRequestExpiryTask;
import dev.revere.alley.profile.ProfileRepository;
import dev.revere.alley.profile.command.*;
import dev.revere.alley.profile.cosmetic.command.CosmeticCommand;
import dev.revere.alley.profile.cosmetic.repository.CosmeticRepository;
import dev.revere.alley.profile.division.DivisionRepository;
import dev.revere.alley.profile.division.command.DivisionCommand;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.profile.listener.ProfileListener;
import dev.revere.alley.profile.settings.command.MatchSettingsCommand;
import dev.revere.alley.profile.settings.command.PracticeSettingsCommand;
import dev.revere.alley.profile.settings.command.toggle.TogglePartyInvitesCommand;
import dev.revere.alley.profile.settings.command.toggle.TogglePartyMessagesCommand;
import dev.revere.alley.profile.settings.command.toggle.ToggleScoreboardCommand;
import dev.revere.alley.profile.settings.command.toggle.ToggleTablistCommand;
import dev.revere.alley.profile.settings.command.worldtime.*;
import dev.revere.alley.profile.shop.command.ShopCommand;
import dev.revere.alley.profile.shop.command.admin.SetCoinsCommand;
import dev.revere.alley.queue.QueueRepository;
import dev.revere.alley.queue.command.admin.QueueCommand;
import dev.revere.alley.queue.command.player.LeaveQueueCommand;
import dev.revere.alley.queue.command.player.QueuesCommand;
import dev.revere.alley.queue.command.player.RankedCommand;
import dev.revere.alley.queue.command.player.UnrankedCommand;
import dev.revere.alley.util.ServerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.logger.Logger;
import dev.revere.alley.visual.leaderboard.command.LeaderboardCommand;
import dev.revere.alley.visual.scoreboard.ScoreboardVisualizer;
import dev.revere.alley.visual.scoreboard.animation.ScoreboardTitleHandler;
import dev.revere.alley.visual.tablist.task.TablistUpdateTask;
import dev.revere.alley.world.WorldListener;
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
        List<String> expectedAuthors = Arrays.asList("Revere Development", "Emmy", "Remi");

        Bukkit.getConsoleSender().sendMessage(CC.translate(prefix + "Expected authors: &a" + expectedAuthors + "&f, Retrieved authors: &c" + authors));

        if (!new HashSet<>(authors).containsAll(expectedAuthors)) {
            System.exit(0);
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
