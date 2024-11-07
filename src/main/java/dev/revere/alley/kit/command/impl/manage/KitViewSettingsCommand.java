package dev.revere.alley.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 20:04
 */
public class KitViewSettingsCommand extends BaseCommand {
    @Command(name = "kit.viewsettings", permission = "alley.admin", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit viewsettings &b<kitName>"));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cA kit with that name does not exist!"));
            return;
        }

        sender.sendMessage("");
        sender.sendMessage(CC.translate("&b&lKit Settings for " + kit.getName()));
        kit.getKitSettings().forEach(setting -> sender.sendMessage(CC.translate(" &f● &b" + setting.getName() + " &f(" + (setting.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        sender.sendMessage("");
    }
}