package me.emmy.pluginbase.commands.global;

import me.emmy.pluginbase.locale.Locale;
import me.emmy.pluginbase.utils.chat.CC;
import me.emmy.pluginbase.utils.command.BaseCommand;
import me.emmy.pluginbase.utils.command.Command;
import me.emmy.pluginbase.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: PluginBase
 * Date: 19/04/2024 - 17:46
 */

public class ProfileCommand extends BaseCommand {
    @Override
    @Command(name = "profile")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage(CC.translate(Locale.DEBUG_CMD));
    }
}