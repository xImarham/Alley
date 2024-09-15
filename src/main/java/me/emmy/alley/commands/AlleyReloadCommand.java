package me.emmy.alley.commands;

import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
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
