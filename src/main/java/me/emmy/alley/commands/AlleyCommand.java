package me.emmy.alley.commands;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/04/2024 - 17:39
 */

public class AlleyCommand extends BaseCommand {
    @Override
    @Command(name = "alley", aliases = {"emmy", "ziue"}, inGameOnly = false)
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage(" ");
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(CC.translate("  &d&l   Alley"));
        sender.sendMessage(CC.translate("      &f┃ Author: &d" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        sender.sendMessage(CC.translate("      &f┃ Version: &d" + Alley.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" "));
        sender.sendMessage(CC.translate("  &d&l   Description:"));
        sender.sendMessage(CC.translate("      &f┃ " + Alley.getInstance().getDescription().getDescription()));
        sender.sendMessage(CC.FLOWER_BAR);
        sender.sendMessage(" ");
    }
}