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
public class FireballSpeedCommand extends BaseCommand {
    @CommandData(name = "fireball.speed", aliases = {"fb.speed"}, isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length == 0) {
            sender.sendMessage(CC.translate("&6Usage: &e/fireball speed &b<value>"));
            return;
        }

        double value;
        try {
            value = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cThe speed must be a valid number."));
            return;
        }

        FireballService fireballService = this.plugin.getFireballService();
        fireballService.setSpeed(value);
        sender.sendMessage(CC.translate("&aSuccessfully set the speed to &b" + value + "&a."));
    }
}