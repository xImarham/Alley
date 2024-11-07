package dev.revere.alley.game.tournament.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 19:07
 */
public class TournamentLeaveCommand extends BaseCommand {
    @Command(name = "tournament.leave")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        /*TournamentRepository tournamentRepository = Alley.getInstance().getTournamentRepository();

        if (!tournamentRepository.isRunning()) {
            player.sendMessage("There is no tournament running.");
            return;
        }

        if (!tournamentRepository.isPlayerInTournament(player)) {
            player.sendMessage("You are not in the tournament.");
            return;
        }

        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        tournamentRepository.getPlayers().remove(player);
        player.sendMessage(CC.translate("&cYou have left the tournament."));*/

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
        //TournamentLogger.broadcastPlayerLeave(player);
    }
}