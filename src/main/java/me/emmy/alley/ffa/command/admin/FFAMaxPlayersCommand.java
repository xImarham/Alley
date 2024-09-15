package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAMaxPlayersCommand extends BaseCommand {
    @Command(name = "ffa.maxplayers", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 2) {
            player.sendMessage(CC.translate("&cUsage: /ffa maxplayers <kit> <maxPlayers>"));
            return;
        }

        String kitName = args[0];
        int maxPlayers = Integer.parseInt(args[1]);

        AbstractFFAMatch match = Alley.getInstance().getFfaRepository().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        match.setMaxPlayers(maxPlayers);
        Alley.getInstance().getFfaRepository().saveFFAMatch(match);
        player.sendMessage(CC.translate("&aSuccessfully set the max players for the FFA match."));
    }
}
