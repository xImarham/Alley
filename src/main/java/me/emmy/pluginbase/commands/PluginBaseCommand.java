package me.emmy.pluginbase.commands;

import me.emmy.pluginbase.PluginBase;
import me.emmy.pluginbase.utils.chat.CC;
import me.emmy.pluginbase.utils.command.BaseCommand;
import me.emmy.pluginbase.utils.command.Command;
import me.emmy.pluginbase.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: PluginBase
 * Date: 19/04/2024 - 17:39
 */

public class PluginBaseCommand extends BaseCommand {
    @Override
    @Command(name = "pluginbase", aliases = "ep", inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage(" ");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("  &b&l   PluginBase"));
        sender.sendMessage(CC.translate("      &f┃ Author: &b" + PluginBase.getInstance().getDescription().getAuthors().get(0)/*.toString().replace("[", "").replace("]", "")*/));
        sender.sendMessage(CC.translate("      &f┃ Version: &b" + PluginBase.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" "));
        sender.sendMessage(CC.translate("  &b&l   Description:"));
        sender.sendMessage(CC.translate("      &f┃ " + PluginBase.getInstance().getDescription().getDescription()));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(" ");
    }
}