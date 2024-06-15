package me.emmy.alley.host.impl.tournament.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.host.impl.tournament.Tournament;
import me.emmy.alley.host.impl.tournament.TournamentLogger;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 15/06/2024 - 19:07
 */
public class TournamentLeaveCommand extends BaseCommand {
    @Override
    @Command(name = "tournament.leave")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Tournament tournament = Alley.getInstance().getTournament();

        if (!tournament.isRunning()) {
            player.sendMessage("There is no tournament running.");
            return;
        }

        if (!tournament.isPlayerInTournament(player)) {
            player.sendMessage("You are not in the tournament.");
            return;
        }

        tournament.getPlayers().remove(player);
        player.sendMessage(CC.translate("&cYou have left the tournament."));
        TournamentLogger.broadcastPlayerLeave(player);
    }
}
