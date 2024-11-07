package dev.revere.alley.game.tournament.command;

import dev.revere.alley.Alley;
import dev.revere.alley.game.tournament.command.impl.TournamentHostCommand;
import dev.revere.alley.game.tournament.command.impl.TournamentJoinCommand;
import dev.revere.alley.game.tournament.command.impl.TournamentLeaveCommand;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:48
 */
public class TournamentCommand extends BaseCommand {

    public TournamentCommand() {
        new TournamentHostCommand();
        new TournamentJoinCommand();
        new TournamentLeaveCommand();
    }
    @Override
    @Command(name = "tournament", permission = "alley.hosttournament")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to host a competition."));
            return;
        }

        /*player.sendMessage("");
        player.sendMessage(CC.translate("&b&lTournament Commands Help"));
        player.sendMessage(CC.translate(" &f● &b/tournament host &7| Host a tournament"));
        player.sendMessage(CC.translate(" &f● &b/tournament join &7| Join a tournament"));
        player.sendMessage(CC.translate(" &f● &b/tournament leave &7| Leave a tournament"));

        if (player.hasPermission("alley.admin")) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&c&lTournament Admin Commands:"));
            player.sendMessage(CC.translate(" &f● &c/tournament forcestart &7| Force start a tournament"));
            player.sendMessage(CC.translate(" &f● &c/tournament skipround &7| Skip a round in a tournament"));
            player.sendMessage(CC.translate(" &f● &c/tournament cancel &7| Cancel a tournament"));
        }
        player.sendMessage("");*/

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
    }
}
