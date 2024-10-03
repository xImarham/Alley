package me.emmy.alley.kit.command.impl.manage;

import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Practice
 * @date 28/04/2024 - 22:07
 */
public class KitListCommand extends BaseCommand {
    @Override
    @Command(name = "kit.list", aliases = {"kits"}, permission = "alley.admin")
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();

        sender.sendMessage("");
        sender.sendMessage(CC.translate("     &b&lKit List &f(" + Alley.getInstance().getKitRepository().getKits().size() + "&f)"));
        if (Alley.getInstance().getKitRepository().getKits().isEmpty()) {
            sender.sendMessage(CC.translate("      &f● &cNo Kits available."));
        }
        Alley.getInstance().getKitRepository().getKits().forEach(kit -> sender.sendMessage(CC.translate("      &f● &b" + kit.getDisplayName() + " &f(" + (kit.isEnabled() ? "&aEnabled" : "&cDisabled") + "&f)")));
        sender.sendMessage("");
    }
}