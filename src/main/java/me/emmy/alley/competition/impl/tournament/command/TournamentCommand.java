package me.emmy.alley.competition.impl.tournament.command;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:48
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
        player.sendMessage(CC.translate("&c&lTournament Help"));
        player.sendMessage(CC.translate(" &7- &c/tournament host"));
        player.sendMessage(CC.translate(" &7- &c/tournament join"));
        player.sendMessage(CC.translate(" &7- &c/tournament leave"));

        if (player.hasPermission("alley.admin")) {
            player.sendMessage(CC.translate(" &7- &c/tournament forcestart"));
            player.sendMessage(CC.translate(" &7- &c/tournament skipround"));
            player.sendMessage(CC.translate(" &7- &9/tournament cancel"));
        }
    }
}
