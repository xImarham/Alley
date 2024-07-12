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
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 18:32
 */
public class TournamentJoinCommand extends BaseCommand {
    @Override
    @Command(name = "tournament.join")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Tournament tournament = Alley.getInstance().getTournament();
        if (!tournament.isRunning()) {
            player.sendMessage(CC.translate("&cThere is no tournament running."));
            return;
        }

        if (tournament.getPlayers().size() >= tournament.getMaxPlayers()) {
            player.sendMessage(CC.translate("&cThe tournament is full."));
            return;
        }

        if (tournament.isPlayerInTournament(player)) {
            player.sendMessage(CC.translate("&cYou have already joined the tournament."));
            return;
        }

        tournament.getPlayers().add(player);
        TournamentLogger.broadcastPlayerJoin(player);
    }
}
