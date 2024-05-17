package me.emmy.alley.commands.admin.kit.subcommands;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.command.BaseCommand;
import me.emmy.alley.utils.command.Command;
import me.emmy.alley.utils.command.CommandArgs;
import org.bukkit.entity.Player;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 28/04/2024 - 22:25
 */

public class KitGetInventoryCommand extends BaseCommand {
    @Override
    @Command(name = "kit.getinventory", permission = "practice.admin")
    public void onCommand(CommandArgs args) {
        Player sender = args.getPlayer();

        if (args.length() < 1) {
            sender.sendMessage(CC.translate("&cUsage: /kit getinventory (kitName)"));
            return;
        }

        String kitName = args.getArgs()[0];

        Kit kit = Alley.getInstance().getKitManager().getKit(kitName);

        if (kit == null) {
            sender.sendMessage(CC.translate("&cThere is no kit with that name."));
            return;
        }

        sender.getInventory().setContents(kit.getItems());
        sender.getInventory().setArmorContents(kit.getArmour());
        Alley.getInstance().getKitManager().saveKit(kitName, kit);
        sender.sendMessage(CC.translate("&aSuccessfully retrieved the inventory of the kit."));
    }
}
