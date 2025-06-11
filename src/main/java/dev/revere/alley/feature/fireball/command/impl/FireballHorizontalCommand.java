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
public class FireballHorizontalCommand extends BaseCommand {
    @CommandData(name = "fireball.horizontal", aliases = {"fb.horizontal"}, isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/fireball horizontal <value>"));
            return;
        }

        double value;
        try {
            value = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cInvalid value. Please enter a valid number."));
            return;
        }

        FireballService fireballService = this.plugin.getFireballService();
        fireballService.setHorizontal(value);
        sender.sendMessage(CC.translate("&aSuccessfully set the fireball knockback (horizontal) value to &b" + value + "&a."));

    }
}
