package dev.revere.alley.command.impl.other;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 22/06/2025
 */
public class SudoAllCommand extends BaseCommand {
    @CommandData(name = "sudoall", isAdminOnly = true, permission = "alley.admin.sudoall")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&cUsage: /sudoall <message>"));
            return;
        }

        String message = String.join(" ", args);
        for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
            onlinePlayer.chat(message);
        }

        player.sendMessage(CC.translate("&aSuccessfully sent message to all players: " + message));
    }
}
