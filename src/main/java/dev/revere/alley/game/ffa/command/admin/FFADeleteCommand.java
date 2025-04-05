package dev.revere.alley.game.ffa.command.admin;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public class FFADeleteCommand extends BaseCommand {
    @CommandData(name = "ffa.delete", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length != 1) {
            player.sendMessage(CC.translate("&cUsage: /ffa delete <kit>"));
            return;
        }

        String kitName = args[0];
        AbstractFFAMatch match = this.plugin.getFfaService().getFFAMatch(kitName);
        if (match == null) {
            player.sendMessage(CC.translate("&cThere is no FFA match with the name " + kitName + "."));
            return;
        }

        this.plugin.getFfaService().deleteFFAMatch(match);
        player.sendMessage(CC.translate("&aSuccessfully deleted the FFA match."));
    }
}
