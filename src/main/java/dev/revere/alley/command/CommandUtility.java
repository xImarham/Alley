package dev.revere.alley.command;

import dev.revere.alley.command.impl.admin.PlaytimeCommand;
import dev.revere.alley.command.impl.donator.HostCommand;
import dev.revere.alley.command.impl.donator.emoji.EmojiCommand;
import dev.revere.alley.command.impl.main.AlleyCommand;
import dev.revere.alley.command.impl.main.AlleyDebugCommand;
import dev.revere.alley.command.impl.main.AlleyReloadCommand;
import dev.revere.alley.essential.command.*;
import dev.revere.alley.essential.command.troll.*;
import dev.revere.alley.feature.arena.command.ArenaCommand;
import dev.revere.alley.feature.cosmetic.command.CosmeticCommand;
import dev.revere.alley.feature.division.command.DivisionCommand;
import dev.revere.alley.feature.kit.command.KitCommand;
import dev.revere.alley.feature.kit.editor.command.KitEditorCommand;
import dev.revere.alley.feature.leaderboard.command.LeaderboardCommand;
import dev.revere.alley.feature.queue.command.admin.QueueCommand;
import dev.revere.alley.feature.queue.command.player.LeaveQueueCommand;
import dev.revere.alley.feature.queue.command.player.QueuesCommand;
import dev.revere.alley.feature.server.command.ServiceCommand;
import dev.revere.alley.feature.spawn.command.SetSpawnCommand;
import dev.revere.alley.feature.spawn.command.SpawnCommand;
import dev.revere.alley.feature.spawn.command.SpawnItemsCommand;
import dev.revere.alley.game.duel.command.AcceptCommand;
import dev.revere.alley.game.duel.command.DuelCommand;
import dev.revere.alley.game.duel.command.DuelRequestsCommand;
import dev.revere.alley.game.ffa.command.admin.FFACommand;
import dev.revere.alley.game.match.command.admin.MatchCommand;
import dev.revere.alley.game.match.command.admin.impl.MatchInfoCommand;
import dev.revere.alley.game.match.command.player.CurrentMatchesCommand;
import dev.revere.alley.game.match.command.player.LeaveMatchCommand;
import dev.revere.alley.game.match.command.player.LeaveSpectatorCommand;
import dev.revere.alley.game.match.command.player.SpectateCommand;
import dev.revere.alley.game.match.snapshot.command.InventoryCommand;
import dev.revere.alley.game.party.command.PartyCommand;
import dev.revere.alley.profile.command.ChallengesCommand;
import dev.revere.alley.profile.command.ChatCommand;
import dev.revere.alley.profile.command.MatchHistoryCommand;
import dev.revere.alley.profile.command.admin.ResetStatsCommand;
import dev.revere.alley.profile.command.admin.ranked.RankedCommand;
import dev.revere.alley.profile.settings.command.MatchSettingsCommand;
import dev.revere.alley.profile.settings.command.PracticeSettingsCommand;
import dev.revere.alley.profile.settings.command.toggle.*;
import dev.revere.alley.profile.settings.command.worldtime.*;
import dev.revere.alley.profile.shop.command.ShopCommand;
import dev.revere.alley.profile.shop.command.admin.SetCoinsCommand;
import dev.revere.alley.profile.stats.command.StatsCommand;
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
            new AlleyReloadCommand();

            new KitCommand();
            new ArenaCommand();
            new MatchCommand();
            new RankedCommand();
            new QueueCommand();
            new FFACommand();
            new CosmeticCommand();
            new DivisionCommand();

            new AlleyDebugCommand();
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
        });
    }
}