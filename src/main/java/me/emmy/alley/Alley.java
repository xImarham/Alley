package me.emmy.alley;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.ArenaRepository;
import me.emmy.alley.arena.command.ArenaCommand;
import me.emmy.alley.arena.command.impl.*;
import me.emmy.alley.arena.listener.ArenaListener;
import me.emmy.alley.commands.AlleyCommand;
import me.emmy.alley.commands.AlleyReloadCommand;
import me.emmy.alley.commands.admin.debug.StateCommand;
import me.emmy.alley.commands.admin.debug.TestCommand;
import me.emmy.alley.commands.admin.essential.EnchantCommand;
import me.emmy.alley.commands.admin.essential.RenameCommand;
import me.emmy.alley.commands.admin.management.PlaytimeCommand;
import me.emmy.alley.commands.donator.HostCommand;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.database.MongoService;
import me.emmy.alley.database.profile.impl.MongoProfileImpl;
import me.emmy.alley.ffa.FFARepository;
import me.emmy.alley.ffa.combat.CombatManager;
import me.emmy.alley.ffa.command.admin.*;
import me.emmy.alley.ffa.command.player.FFAJoinCommand;
import me.emmy.alley.ffa.command.player.FFALeaveCommand;
import me.emmy.alley.ffa.listener.FFAListener;
import me.emmy.alley.ffa.safezone.FFASpawnHandler;
import me.emmy.alley.ffa.safezone.task.FFASpawnTask;
import me.emmy.alley.host.impl.event.command.EventCommand;
import me.emmy.alley.host.impl.tournament.Tournament;
import me.emmy.alley.host.impl.tournament.command.TournamentCommand;
import me.emmy.alley.host.impl.tournament.command.impl.TournamentHostCommand;
import me.emmy.alley.host.impl.tournament.command.impl.TournamentJoinCommand;
import me.emmy.alley.host.impl.tournament.command.impl.TournamentLeaveCommand;
import me.emmy.alley.hotbar.HotbarRepository;
import me.emmy.alley.hotbar.listener.HotbarListener;
import me.emmy.alley.kit.KitRepository;
import me.emmy.alley.kit.command.KitCommand;
import me.emmy.alley.kit.command.impl.data.KitSetDescriptionCommand;
import me.emmy.alley.kit.command.impl.data.KitSetDisplayNameCommand;
import me.emmy.alley.kit.command.impl.data.KitSetIconCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitGetInvCommand;
import me.emmy.alley.kit.command.impl.data.inventory.KitSetInvCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetEditorSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetRankedSlotCommand;
import me.emmy.alley.kit.command.impl.data.slot.KitSetUnrankedSlotCommand;
import me.emmy.alley.kit.command.impl.manage.KitCreateCommand;
import me.emmy.alley.kit.command.impl.manage.KitDeleteCommand;
import me.emmy.alley.kit.command.impl.manage.KitListCommand;
import me.emmy.alley.kit.command.impl.manage.KitViewCommand;
import me.emmy.alley.kit.command.impl.settings.KitSetSettingCommand;
import me.emmy.alley.kit.command.impl.settings.KitSettingsCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveAllCommand;
import me.emmy.alley.kit.command.impl.storage.KitSaveCommand;
import me.emmy.alley.kit.settings.KitSettingRepository;
import me.emmy.alley.leaderboard.command.LeaderboardCommand;
import me.emmy.alley.match.MatchRepository;
import me.emmy.alley.match.command.admin.MatchCommand;
import me.emmy.alley.match.command.admin.impl.MatchCancelCommand;
import me.emmy.alley.match.command.admin.impl.MatchStartCommand;
import me.emmy.alley.match.command.player.CurrentMatchesCommand;
import me.emmy.alley.match.command.player.LeaveMatchCommand;
import me.emmy.alley.match.command.player.LeaveSpectatorCommand;
import me.emmy.alley.match.command.player.SpectateCommand;
import me.emmy.alley.match.listener.MatchListener;
import me.emmy.alley.match.player.visibility.PlayerVisibility;
import me.emmy.alley.match.snapshot.SnapshotRepository;
import me.emmy.alley.match.snapshot.command.InventoryCommand;
import me.emmy.alley.party.PartyRepository;
import me.emmy.alley.party.PartyRequest;
import me.emmy.alley.party.command.PartyCommand;
import me.emmy.alley.party.command.impl.leader.PartyCreateCommand;
import me.emmy.alley.party.command.impl.leader.PartyDisbandCommand;
import me.emmy.alley.party.command.impl.leader.PartyInviteCommand;
import me.emmy.alley.party.command.impl.leader.PartyKickCommand;
import me.emmy.alley.party.command.impl.member.PartyAcceptCommand;
import me.emmy.alley.party.command.impl.member.PartyChatCommand;
import me.emmy.alley.party.command.impl.member.PartyInfoCommand;
import me.emmy.alley.party.command.impl.member.PartyLeaveCommand;
import me.emmy.alley.party.listener.PartyListener;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.command.ProfileMenuCommand;
import me.emmy.alley.profile.cosmetic.command.CosmeticCommand;
import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticGetSelectedCommand;
import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticListCommand;
import me.emmy.alley.profile.cosmetic.command.impl.admin.CosmeticSetCommand;
import me.emmy.alley.profile.cosmetic.command.impl.player.CosmeticsCommand;
import me.emmy.alley.profile.cosmetic.repository.CosmeticRepository;
import me.emmy.alley.profile.division.DivisionRepository;
import me.emmy.alley.profile.division.command.DivisionCommand;
import me.emmy.alley.profile.division.command.impl.DivisionListCommand;
import me.emmy.alley.profile.division.command.impl.DivisionMenuCommand;
import me.emmy.alley.profile.listener.ProfileListener;
import me.emmy.alley.profile.settings.matchsettings.command.MatchSettingsCommand;
import me.emmy.alley.profile.settings.playersettings.command.SettingsCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.TogglePartyInvitesCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.TogglePartyMessagesCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.ToggleScoreboardCommand;
import me.emmy.alley.profile.settings.playersettings.command.toggle.ToggleTablistCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.CurrentTimeCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.DayCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.NightCommand;
import me.emmy.alley.profile.settings.playersettings.command.worldtime.SunsetCommand;
import me.emmy.alley.profile.shop.command.ShopCommand;
import me.emmy.alley.profile.shop.command.admin.SetCoinsCommand;
import me.emmy.alley.queue.QueueRepository;
import me.emmy.alley.queue.command.admin.ForceQueueCommand;
import me.emmy.alley.queue.command.admin.QueueReloadCommand;
import me.emmy.alley.queue.command.player.LeaveQueueCommand;
import me.emmy.alley.queue.command.player.QueuesCommand;
import me.emmy.alley.queue.command.player.RankedCommand;
import me.emmy.alley.queue.command.player.UnrankedCommand;
import me.emmy.alley.scoreboard.ScoreboardAdapter;
import me.emmy.alley.scoreboard.handler.ScoreboardHandler;
import me.emmy.alley.spawn.SpawnHandler;
import me.emmy.alley.spawn.command.SetSpawnCommand;
import me.emmy.alley.spawn.command.SpawnCommand;
import me.emmy.alley.spawn.command.SpawnItemsCommand;
import me.emmy.alley.spawn.listener.SpawnListener;
import me.emmy.alley.utils.assemble.Assemble;
import me.emmy.alley.utils.assemble.AssembleStyle;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.chat.Logger;
import me.emmy.alley.utils.command.CommandFramework;
import me.emmy.alley.utils.menu.MenuListener;
import me.emmy.alley.utils.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
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

    private KitSettingRepository kitSettingRepository;
    private SnapshotRepository snapshotRepository;
    private CooldownRepository cooldownRepository;
    private CosmeticRepository cosmeticRepository;
    private DivisionRepository divisionRepository;
    private ProfileRepository profileRepository;
    private ScoreboardHandler scoreboardHandler;
    private PlayerVisibility playerVisibility;
    private HotbarRepository hotbarRepository;
    private CommandFramework commandFramework;
    private FFASpawnHandler ffaSpawnHandler;
    private ArenaRepository arenaRepository;
    private MatchRepository matchRepository;
    private QueueRepository queueRepository;
    private PartyRepository partyRepository;
    private CombatManager combatManager;
    private KitRepository kitRepository;
    private FFARepository ffaRepository;
    private ConfigHandler configHandler;
    private SpawnHandler spawnHandler;
    private MongoService mongoService;
    private PartyRequest partyRequest;
    private Tournament tournament;

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
        setupWorld();
        loadTasks();

        long end = System.currentTimeMillis();
        long timeTaken = end - start;

        CC.pluginEnabled(timeTaken);
    }

    @Override
    public void onDisable() {
        ServerUtil.disconnectPlayers();

        profileRepository.getProfiles().forEach((uuid, profile) -> profile.save());
        kitRepository.saveKits();
        ffaRepository.saveFFAMatches();

        for (Entity entity : this.getServer().getWorld("world").getEntities()) {
            if (entity.getType() == EntityType.DROPPED_ITEM) {
                entity.remove();
            }
        }

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
        scoreboardHandler = new ScoreboardHandler();
    }

    private String registerDatabase() {
        FileConfiguration config = configHandler.getConfigByName("database/database.yml");
        return config.getString("mongo.uri");
    }

    private void registerManagers() {
        Logger.logTime("CommandFramework", () -> this.commandFramework = new CommandFramework(this));
        Logger.logTime("QueueRepository", () -> {
            this.queueRepository = new QueueRepository();
            this.queueRepository.initialize();
        });
        Logger.logTime("KitSettingRepository", () -> this.kitSettingRepository = new KitSettingRepository());
        Logger.logTime("KitRepository", () -> {
            this.kitRepository = new KitRepository();
            this.kitRepository.loadKits();
        });
        Logger.logTime("ArenaRepository", () -> {
            this.arenaRepository = new ArenaRepository();
            this.arenaRepository.loadArenas();
        });
        Logger.logTime("FFARepository", () -> {
            this.ffaRepository = new FFARepository();
            this.ffaRepository.loadFFAMatches();
        });
        Logger.logTime("CosmeticRepository", () -> this.cosmeticRepository = new CosmeticRepository());
        Logger.logTime("ProfileRepository", () -> {
            this.profileRepository = new ProfileRepository();
            this.profileRepository.setIProfile(new MongoProfileImpl());
        });
        Logger.logTime("DivisionRepository", () -> this.divisionRepository = new DivisionRepository());
        Logger.logTime("MongoService", () -> this.mongoService = new MongoService(registerDatabase()));
        Logger.logTime("HotbarRepository", () -> this.hotbarRepository = new HotbarRepository());
        Logger.logTime("profiles", () -> this.profileRepository.loadProfiles());
        Logger.logTime("CooldownRepository", () -> this.cooldownRepository = new CooldownRepository());
        Logger.logTime("SnapshotRepository", () -> this.snapshotRepository = new SnapshotRepository());
        Logger.logTime("MatchRepository", () -> this.matchRepository = new MatchRepository());
        Logger.logTime("PartyRepository", () -> this.partyRepository = new PartyRepository());
        Logger.logTime("SpawnHandler", () -> {
            this.spawnHandler = new SpawnHandler();
            this.spawnHandler.loadSpawnLocation();
        });
        Logger.logTime("CombatManager", () -> this.combatManager = new CombatManager());
        Logger.logTime("FFASpawnHandler", ()-> {
            this.ffaSpawnHandler = new FFASpawnHandler();
            this.ffaSpawnHandler.loadFFASpawn();
        });
        Logger.logTime("PlayerVisibility", () -> this.playerVisibility = new PlayerVisibility());
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
                new FFAListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        Logger.logTime("Admin Commands", () -> {
            new AlleyCommand();
            new AlleyReloadCommand();

            new SpawnItemsCommand();
            new SetSpawnCommand();
            new SpawnCommand();

            new PlaytimeCommand();
            new RenameCommand();
            new EnchantCommand();

            new KitCommand();
            new KitSaveCommand();
            new KitSaveAllCommand();
            new KitCreateCommand();
            new KitDeleteCommand();
            new KitListCommand();
            new KitGetInvCommand();
            new KitSetInvCommand();
            new KitSetDescriptionCommand();
            new KitSetDisplayNameCommand();
            new KitSetEditorSlotCommand();
            new KitSetUnrankedSlotCommand();
            new KitSetRankedSlotCommand();
            new KitSetSettingCommand();
            new KitSettingsCommand();
            new KitSetIconCommand();
            new KitViewCommand();

            new ArenaSetSafeZoneCommand();
            new ArenaSetCenterCommand();
            new ArenaCreateCommand();
            new ArenaSetCuboidCommand();
            new ArenaTeleportCommand();
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

            new ForceQueueCommand();
            new QueueReloadCommand();

            new MatchCommand();
            new MatchStartCommand();
            new MatchCancelCommand();

            new FFACommand();
            new FFACreateCommand();
            new FFADeleteCommand();
            new FFAKickCommand();
            new FFAListCommand();
            new FFAListPlayersCommand();
            new FFAMaxPlayersCommand();

            new CosmeticCommand();
            new CosmeticsCommand();
            new CosmeticListCommand();
            new CosmeticSetCommand();
            new CosmeticGetSelectedCommand();

            new DivisionCommand();
            new DivisionMenuCommand();
            new DivisionListCommand();

            new SetCoinsCommand();

            //debugging
            new StateCommand();
        });

        Logger.logTime("Donator Command", () -> {
            new HostCommand();

            new EventCommand();

            new TournamentCommand();
            new TournamentHostCommand();
            new TournamentJoinCommand();
            new TournamentLeaveCommand();
        });

        Logger.logTime("Player Commands", () -> {

            new DayCommand();
            new NightCommand();
            new SunsetCommand();
            new CurrentTimeCommand();
            new TogglePartyInvitesCommand();
            new TogglePartyMessagesCommand();
            new ToggleScoreboardCommand();
            new ToggleTablistCommand();

            new PartyCommand();
            new PartyCreateCommand();
            new PartyLeaveCommand();
            new PartyInfoCommand();
            new PartyChatCommand();
            new PartyInviteCommand();
            new PartyAcceptCommand();
            new PartyDisbandCommand();
            new PartyKickCommand();

            new FFAJoinCommand();
            new FFALeaveCommand();

            new UnrankedCommand();
            new RankedCommand();
            new SettingsCommand();
            new LeaderboardCommand();
            new SpectateCommand();
            new LeaveSpectatorCommand();
            new LeaveMatchCommand();
            new CurrentMatchesCommand();
            new LeaveQueueCommand();
            new QueuesCommand();
            new ProfileMenuCommand();
            new MatchSettingsCommand();

            new InventoryCommand();

            new ShopCommand();
            new TestCommand();
        });
    }

    private void loadScoreboard() {
        Assemble assemble = new Assemble(this, new ScoreboardAdapter());
        assemble.setTicks(2);
        assemble.setAssembleStyle(AssembleStyle.MODERN);
    }

    private void setupWorld() {
        for (World world : Bukkit.getWorlds()) {
            world.setDifficulty(Difficulty.EASY);
            world.setStorm(false);
            world.setThundering(false);
            world.setTime(6000);
        }
    }

    private void loadTasks() {
        new FFASpawnTask(ffaSpawnHandler.getCuboid(), Alley.getInstance()).runTaskTimer(Alley.getInstance(), 0, 20);
    }

    public FileConfiguration getConfig(String fileName) {
        File configFile = new File(getDataFolder(), fileName);
        return YamlConfiguration.loadConfiguration(configFile);
    }
}
