package dev.revere.alley.command;

import dev.revere.alley.command.impl.main.AlleyCommand;
import dev.revere.alley.command.impl.main.impl.AlleyCoreCommand;
import dev.revere.alley.command.impl.main.impl.AlleyDebugCommand;
import dev.revere.alley.command.impl.main.impl.AlleyReloadCommand;
import dev.revere.alley.command.impl.other.*;
import dev.revere.alley.command.impl.other.troll.*;
import dev.revere.alley.feature.emoji.command.EmojiCommand;
import dev.revere.alley.feature.level.command.LevelAdminCommand;
import dev.revere.alley.feature.tip.command.TipCommand;
import dev.revere.alley.base.arena.command.ArenaCommand;
import dev.revere.alley.feature.cosmetic.command.CosmeticCommand;
import dev.revere.alley.feature.division.command.DivisionCommand;
import dev.revere.alley.base.kit.command.KitCommand;
import dev.revere.alley.base.kit.editor.command.KitEditorCommand;
import dev.revere.alley.feature.layout.command.LayoutCommand;
import dev.revere.alley.feature.leaderboard.command.LeaderboardCommand;
import dev.revere.alley.profile.command.player.LevelCommand;
import dev.revere.alley.base.queue.command.admin.QueueCommand;
import dev.revere.alley.base.queue.command.player.LeaveQueueCommand;
import dev.revere.alley.base.queue.command.player.QueuesCommand;
import dev.revere.alley.feature.server.command.ServiceCommand;
import dev.revere.alley.base.spawn.command.SetSpawnCommand;
import dev.revere.alley.base.spawn.command.SpawnCommand;
import dev.revere.alley.base.spawn.command.SpawnItemsCommand;
import dev.revere.alley.feature.title.command.TitleCommand;
import dev.revere.alley.game.duel.command.AcceptCommand;
import dev.revere.alley.game.duel.command.DuelCommand;
import dev.revere.alley.game.duel.command.DuelRequestsCommand;
import dev.revere.alley.game.ffa.command.FFACommand;
import dev.revere.alley.game.host.command.HostCommand;
import dev.revere.alley.game.match.command.admin.MatchCommand;
import dev.revere.alley.game.match.command.admin.impl.MatchInfoCommand;
import dev.revere.alley.game.match.command.player.CurrentMatchesCommand;
import dev.revere.alley.game.match.command.player.LeaveMatchCommand;
import dev.revere.alley.game.match.command.player.LeaveSpectatorCommand;
import dev.revere.alley.game.match.command.player.SpectateCommand;
import dev.revere.alley.game.match.snapshot.command.InventoryCommand;
import dev.revere.alley.game.party.command.PartyCommand;
import dev.revere.alley.profile.command.admin.PlaytimeCommand;
import dev.revere.alley.profile.command.admin.ranked.RankedCommand;
import dev.revere.alley.profile.command.admin.statistic.ResetStatsCommand;
import dev.revere.alley.profile.command.admin.statistic.SetCoinsCommand;
import dev.revere.alley.profile.command.player.*;
import dev.revere.alley.profile.command.player.setting.MatchSettingsCommand;
import dev.revere.alley.profile.command.player.setting.PracticeSettingsCommand;
import dev.revere.alley.profile.command.player.setting.toggle.*;
import dev.revere.alley.profile.command.player.setting.worldtime.*;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.logger.command.ViewErrorCommand;
import lombok.experimental.UtilityClass;

/**
 * @author Emmy
 * @project Alley
 * @date 31/12/2024 - 23:24
 */
@UtilityClass
public class CommandUtility {
    /**
     * Registers all commands.
     */
    public void registerCommands() {
        Logger.logTimeWithAction("registered", "Admin Commands", () -> {
            new AlleyCommand();

            new AlleyCoreCommand();
            new AlleyDebugCommand();
            new AlleyReloadCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new RankedCommand();
            new QueueCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();
            new TitleCommand();
            new LevelAdminCommand();
            new LevelCommand();
            new MatchInfoCommand();
            new ServiceCommand();
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

            new ViewErrorCommand();
        });

        Logger.logTimeWithAction("registered", "Donator Commands", () -> {
            new HostCommand();
            new EmojiCommand();
        });

        Logger.logTimeWithAction("registered", "Player Commands", () -> {
            new ChatCommand();
            new DayCommand();
            new NightCommand();
            new SunsetCommand();
            new ResetTimeCommand();
            new TogglePartyInvitesCommand();
            new TogglePartyMessagesCommand();
            new ToggleScoreboardCommand();
            new ToggleScoreboardLinesCommand();
            new ToggleTablistCommand();
            new ToggleWorldTimeCommand();
            new ToggleProfanityFilterCommand();

            new PartyCommand();
            new AcceptCommand();
            new DuelCommand();
            new DuelRequestsCommand();
            new InventoryCommand();
            new PracticeSettingsCommand();
            new LeaderboardCommand();
            new ResetStatsCommand();
            new StatsCommand();
            new SpectateCommand();
            new LeaveSpectatorCommand();
            new LeaveMatchCommand();
            new CurrentMatchesCommand();
            new LeaveQueueCommand();
            new QueuesCommand();
            new MatchSettingsCommand();

            new ShopCommand();
            new ChallengesCommand();
            new MatchHistoryCommand();
            new KitEditorCommand();
            new TipCommand();
            new LayoutCommand();
        });
    }
}