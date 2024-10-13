package me.emmy.alley.essential.command.troll;

import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 20:26
 */
public class PushCommand extends BaseCommand {
    @Command(name = "push", permission = "delta.command.push", usage = "push (player) (value)", description = "Push a player")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/push &b<player> <value>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        double value;

        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid number."));
            return;
        }

        if (value > 10) {
            player.sendMessage(CC.translate("&cValue cannot be greater than 10."));
            return;
        }

        target.setVelocity(player.getLocation().getDirection().multiply(value));

        player.sendMessage(CC.translate("&fYou've pushed &b" + target.getName()));
        target.sendMessage(CC.translate("&fYou've been pushed by &b" + player.getName()));
    }
}