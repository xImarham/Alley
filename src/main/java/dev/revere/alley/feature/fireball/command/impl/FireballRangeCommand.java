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
public class FireballRangeCommand extends BaseCommand {
    @CommandData(name = "fireball.range", aliases = {"fb.range"}, isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/fireball range &b<value>"));
            return;
        }

        double value;
        try {
            value = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cThe range must be a valid number."));
            return;
        }

        FireballService fireballService = this.plugin.getFireballService();
        fireballService.setRange(value);
        sender.sendMessage(CC.translate("&aSuccessfully set the fireball range to &b" + value + "&a blocks."));
    }
}