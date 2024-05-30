package me.emmy.alley.tournament.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.tournament.Tournament;
import me.emmy.alley.tournament.TournamentType;
import me.emmy.alley.tournament.enums.EnumTournamentState;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:57
 */
public class TournamentForceStartCommand extends BaseCommand {
    @Override
    @Command(name = "tournament.forcestart", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        Tournament tournament = Alley.getInstance().getTournamentRepository().getTournaments().stream().filter(t -> t.getPlayers().contains(player)).findFirst().orElse(null);

        if (tournament == null) {
            player.sendMessage("You are not in a tournament.");
            return;
        }

        if (tournament.getPlayers().size() < tournament.getTeamSize()) {
            player.sendMessage("You need at least " + tournament.getTeamSize() + " players to start the tournament.");
            return;
        }

        if (tournament.getEnumTournamentState().equals(EnumTournamentState.FIGHTING)) {
            player.sendMessage("The tournament has already started.");
            return;
        }

        tournament.start();
    }
}
