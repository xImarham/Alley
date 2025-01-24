package dev.revere.alley.game.ffa.command.admin;

import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
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
