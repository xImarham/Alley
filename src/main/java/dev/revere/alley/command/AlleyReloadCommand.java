package dev.revere.alley.command;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 06/06/2024 - 17:34
 */
public class AlleyReloadCommand extends BaseCommand {
    @Override
    @Command(name = "alley.reload", permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage("");
        player.sendMessage(CC.translate("&eReloading &b&lAlley&e..."));
        Alley.getInstance().getConfigHandler().reloadConfigs();
        player.sendMessage(CC.translate("&b&lAlley &ehas been reloaded."));
        player.sendMessage("");
    }
}
