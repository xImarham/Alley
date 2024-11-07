package dev.revere.alley.kit.command.impl.data.inventory;

import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.locale.Locale;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.Command;
import dev.revere.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @Command(name = "kit.setinventory", aliases = "kit.setinv", permission = "practice.admin")
    @Override
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        if (args.length() < 1) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setinventory &b<kitName>"));
            return;
        }

        String kitName = args.getArgs()[0];

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(CC.translate(Locale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        kit.setInventory(sender.getInventory().getContents());
        kit.setArmor(sender.getInventory().getArmorContents());
        Alley.getInstance().getKitRepository().saveKit(kit);
        sender.sendMessage(CC.translate(Locale.KIT_INVENTORY_SET.getMessage().replace("{kit-name}", kitName)));
    }
}