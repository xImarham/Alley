package me.emmy.alley.tournament.command.admin;

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
    }
}
