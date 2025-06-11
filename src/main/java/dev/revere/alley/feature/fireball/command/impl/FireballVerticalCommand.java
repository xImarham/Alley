package dev.revere.alley.feature.fireball.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.fireball.FireballService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
public class FireballVerticalCommand extends BaseCommand {
    @CommandData(name = "fireball.vertical", aliases = {"fb.vertical"}, isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/fireball vertical &b<value>"));
            return;
        }

        double value;
        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cThe value must be a number."));
            return;
        }

        FireballService fireballService = this.plugin.getFireballService();
        fireballService.setVertical(value);
        sender.sendMessage(CC.translate("&aSuccessfully set the fireball knockback (vertical) value to &b" + value + "&a."));
    }
}
