package dev.revere.alley.essential.command;

import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 20/06/2024 - 01:15
 */
public class InvSeeCommand extends BaseCommand {
    @Override
    @Command(name = "invsee", aliases = {"seeinventory", "seeinv"}, permission = "alley.command.invsee")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length == 0) {
            player.sendMessage(CC.translate("&cUsage: /invsee (player)"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        player.openInventory(target.getInventory());
    }
}
