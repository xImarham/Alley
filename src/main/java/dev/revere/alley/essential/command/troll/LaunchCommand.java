package dev.revere.alley.essential.command.troll;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Remi
 * @project Delta
 * @date 6/19/2024
 */
public class LaunchCommand extends BaseCommand {
    @Command(name = "launch", permission = "delta.troll.launch", description = "Launch a player", usage = "/launch <player> | all")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&6Usage: &e/launch &b<player> &7| &ball"));
            return;
        }

        if (args[0].equalsIgnoreCase("all")) {
            player.getServer().getOnlinePlayers().forEach(target -> target.setVelocity(new Vector(0, 1, 0).multiply(15)));
            player.sendMessage(CC.translate("&fYou've launched all players"));
            return;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        target.setVelocity(new Vector(0, 1, 0).multiply(15));
        player.sendMessage(CC.translate("&fYou've launched &b" + target.getName()));
    }
}