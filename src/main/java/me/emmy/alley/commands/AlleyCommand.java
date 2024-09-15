package me.emmy.alley.commands;

import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @Override
    @Command(name = "alley", aliases = {"emmy", "ziue"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage(" ");
        sender.sendMessage(CC.translate("  &b&l   Alley"));
        sender.sendMessage(CC.translate("      &f┃ Authors: &b" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        sender.sendMessage(CC.translate("      &f┃ Version: &b" + Alley.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" "));
        sender.sendMessage(CC.translate("  &b&l   Description:"));
        sender.sendMessage(CC.translate("      &f┃ " + Alley.getInstance().getDescription().getDescription()));
        sender.sendMessage(" ");
    }
}