package dev.revere.alley.essential.command.troll;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/06/2024 - 19:42
 */
public class StrikeCommand extends BaseCommand {
    @CommandData(name = "strike", permission = "alley.command.troll.strike", usage = "strike <player> | all", description = "Strike a player with lightning")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/strike &b<player>"));
            return;
        }

        String targetName = args[0];
        Player target = player.getServer().getPlayer(targetName);

        if (target == null) {
            player.sendMessage(CC.translate("&cPlayer not found."));
            return;
        }

        target.getWorld().strikeLightning(target.getLocation());
        player.sendMessage(CC.translate("&fYou've struck &b" + target.getName()));
    }
}