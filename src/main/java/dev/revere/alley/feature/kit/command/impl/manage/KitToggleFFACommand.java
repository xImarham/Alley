package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 11/04/2025
 */
public class KitToggleFFACommand extends BaseCommand {
    @CommandData(name = "kit.toggleffa", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit toggleffa &b<kitName> <true/false>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        boolean ffaEnabled;
        try {
            ffaEnabled = Boolean.parseBoolean(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cThe value must be true or false."));
            return;
        }

        kit.setFfaEnabled(ffaEnabled);
        kitService.saveKit(kit);
        this.plugin.getFfaService().reloadFFAKits();
        player.sendMessage(CC.translate("&aFFA mode has been " + (ffaEnabled ? "&aenabled" : "&cdisabled") + " for kit &b" + kit.getName() + "&a!"));
        player.sendMessage(CC.translate("&7Additionally, all FFA matches have been reloaded."));
    }
}