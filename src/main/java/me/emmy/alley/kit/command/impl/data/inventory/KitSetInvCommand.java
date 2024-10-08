package me.emmy.alley.kit.command.impl.data.inventory;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.locale.Locale;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Practice
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @Override
    @Command(name = "kit.setinventory", aliases = "kit.setinv", permission = "practice.admin")
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

