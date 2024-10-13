package me.emmy.alley.essential.command.troll;

import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Delta
 * @date 25/06/2024 - 19:42
 */
public class StrikeCommand extends BaseCommand {
    @Command(name = "strike", permission = "delta.command.strike", usage = "strike <player> | all", description = "Strike a player with lightning")
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