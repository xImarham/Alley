package me.emmy.alley.game.tournament.command.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.game.tournament.TournamentLogger;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 14/06/2024 - 23:06
 */
public class TournamentHostCommand extends BaseCommand {
    @Command(name = "tournament.host", permission = "alley.host.tournament")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        /*if (args.length < 2) {
            player.sendMessage(CC.translate("&cUsage: /tournament host <kit> <maxPlayers>"));
            return;
        }

        String kitName = args[0];
        if (Alley.getInstance().getKitRepository().getKit(kitName) == null) {
            player.sendMessage(CC.translate("&cThe kit &f" + kitName + " &cdoes not exist."));
            return;
        }

        int maxPlayers;
        try {
            maxPlayers = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number format for maxPlayers."));
            return;
        }

        if (maxPlayers < 8) {
            player.sendMessage(CC.translate("&cThe maximum amount of players must be at least 8."));
            return;
        }

        TournamentRepository currentTournamentRepository = Alley.getInstance().getTournamentRepository();
        if (currentTournamentRepository != null && currentTournamentRepository.isRunning()) {
            player.sendMessage(CC.translate("&cThere is already a tournament running."));
            return;
        }

        TournamentRepository tournamentRepository = new TournamentRepository(player, Alley.getInstance().getKitRepository().getKit(kitName), 8, maxPlayers);
        Alley.getInstance().setTournamentRepository(tournamentRepository);
        tournamentRepository.setRunning(true);*/

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
        //TournamentLogger.broadcastWaiting();
    }
}