package dev.revere.alley.command;

import dev.revere.alley.arena.command.ArenaCommand;
import dev.revere.alley.command.impl.admin.debug.FFAStateCommand;
import dev.revere.alley.command.impl.admin.debug.StateCommand;
import dev.revere.alley.command.impl.admin.management.PlaytimeCommand;
import dev.revere.alley.command.impl.donator.HostCommand;
import dev.revere.alley.command.impl.donator.emoji.EmojiCommand;
import dev.revere.alley.command.impl.main.AlleyCommand;
import dev.revere.alley.command.impl.main.AlleyReloadCommand;
import dev.revere.alley.essential.command.*;
import dev.revere.alley.essential.command.troll.*;
import dev.revere.alley.essential.spawn.command.SetSpawnCommand;
import dev.revere.alley.essential.spawn.command.SpawnCommand;
import dev.revere.alley.essential.spawn.command.SpawnItemsCommand;
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
import dev.revere.alley.kit.command.KitCommand;
import dev.revere.alley.kit.editor.command.KitEditorCommand;
import dev.revere.alley.leaderboard.command.LeaderboardCommand;
import dev.revere.alley.profile.command.ChallengesCommand;
import dev.revere.alley.profile.command.ChatCommand;
import dev.revere.alley.profile.command.MatchHistoryCommand;
import dev.revere.alley.profile.command.admin.ResetStatsCommand;
import dev.revere.alley.cosmetic.command.CosmeticCommand;
import dev.revere.alley.division.command.DivisionCommand;
import dev.revere.alley.profile.settings.command.MatchSettingsCommand;
import dev.revere.alley.profile.settings.command.PracticeSettingsCommand;
import dev.revere.alley.profile.settings.command.toggle.TogglePartyInvitesCommand;
import dev.revere.alley.profile.settings.command.toggle.TogglePartyMessagesCommand;
import dev.revere.alley.profile.settings.command.toggle.ToggleScoreboardCommand;
import dev.revere.alley.profile.settings.command.toggle.ToggleTablistCommand;
import dev.revere.alley.profile.settings.command.worldtime.*;
import dev.revere.alley.profile.shop.command.ShopCommand;
import dev.revere.alley.profile.shop.command.admin.SetCoinsCommand;
import dev.revere.alley.queue.command.admin.QueueCommand;
import dev.revere.alley.queue.command.player.LeaveQueueCommand;
import dev.revere.alley.queue.command.player.QueuesCommand;
import dev.revere.alley.queue.command.player.RankedCommand;
import dev.revere.alley.queue.command.player.UnrankedCommand;
import dev.revere.alley.profile.stats.command.StatsCommand;
import dev.revere.alley.util.logger.Logger;
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