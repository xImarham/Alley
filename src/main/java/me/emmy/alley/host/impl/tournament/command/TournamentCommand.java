package me.emmy.alley.host.impl.tournament.command;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:48
 */
public class TournamentCommand extends BaseCommand {
    @Override
    @Command(name = "tournament", permission = "alley.hosttournament")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getState() != EnumProfileState.LOBBY) {
            player.sendMessage(CC.translate("&cYou must be in the lobby to host a competition."));
            return;
        }

        player.sendMessage("");
        player.sendMessage(CC.translate("&d&lTournament Commands Help"));
        player.sendMessage(CC.translate(" &f● &d/tournament host &7| Host a tournament"));
        player.sendMessage(CC.translate(" &f● &d/tournament join &7| Join a tournament"));
        player.sendMessage(CC.translate(" &f● &d/tournament leave &7| Leave a tournament"));

        if (player.hasPermission("alley.admin")) {
            player.sendMessage(" ");
            player.sendMessage(CC.translate("&c&lTournament Admin Commands:"));
            player.sendMessage(CC.translate(" &f● &c/tournament forcestart &7| Force start a tournament"));
            player.sendMessage(CC.translate(" &f● &c/tournament skipround &7| Skip a round in a tournament"));
            player.sendMessage(CC.translate(" &f● &c/tournament cancel &7| Cancel a tournament"));
        }
        player.sendMessage("");
    }
}
