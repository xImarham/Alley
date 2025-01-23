package dev.revere.alley.command.impl.main;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:39
 */
public class AlleyCommand extends BaseCommand {
    @Command(name = "alley", aliases = {"emmy", "remi", "revere"}, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        sender.sendMessage("");
        sender.sendMessage(CC.translate("  &b&l   Alley"));
        sender.sendMessage(CC.translate("      &f┃ Authors: &b" + Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", "")));
        sender.sendMessage(CC.translate("      &f┃ Version: &b" + Alley.getInstance().getDescription().getVersion()));
        sender.sendMessage(CC.translate(" "));
        sender.sendMessage(CC.translate("  &b&l   Description:"));
        sender.sendMessage(CC.translate("      &f┃ " + Alley.getInstance().getDescription().getDescription()));
        sender.sendMessage("");
    }
}