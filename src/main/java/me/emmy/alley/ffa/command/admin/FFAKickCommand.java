package me.emmy.alley.ffa.command.admin;

import me.emmy.alley.Alley;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFAKickCommand extends BaseCommand {
    @Command(name = "ffa.kick", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        // /ffa kick <player>

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa kick <player>"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cThere is no player with the name " + args[0] + "."));
            return;
        }

        AbstractFFAMatch match = Alley.getInstance().getFfaRepository().getFFAMatch(target);
        if (match == null) {
            player.sendMessage(CC.translate("&cThis player is not in a FFA match."));
            return;
        }

        match.leave(target);
        target.sendMessage(CC.translate("&cYou have been kicked from the FFA match."));
        player.sendMessage(CC.translate("&aSuccessfully kicked " + target.getName() + " from the FFA match."));
    }
}
