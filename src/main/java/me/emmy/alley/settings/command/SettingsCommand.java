package me.emmy.alley.settings.command;

import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/05/2024 - 11:27
 */

public class SettingsCommand extends BaseCommand {
    @Override
    @Command(name = "settings")
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        player.sendMessage(CC.translate(ErrorMessage.DEBUG_CMD));
    }
}
