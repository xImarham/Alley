package dev.revere.alley.kit.command.impl.data.slot;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.Command;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 03/10/2024 - 15:55
 */
public class KitSetSlotAllCommand extends BaseCommand {
    @Command(name = "kit.setslotall", permission = "alley.command.kit.setslotall", inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setslotall &b<kitName> <slot>"));
            return;
        }

        String kitName = args[0];
        int slot;

        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate("&cUsage: /kit setslotall (kit-name) (slot)"));
            return;
        }

        Kit kit = Alley.getInstance().getKitRepository().getKit(kitName);
        if (kit == null) {
            sender.sendMessage(CC.translate("&cKit not found."));
            return;
        }

        kit.setEditorslot(slot);
        kit.setRankedslot(slot);
        kit.setUnrankedslot(slot);
        Alley.getInstance().getKitRepository().saveKit(kit);
        sender.sendMessage(CC.translate(Alley.getInstance().getConfigService().getMessagesConfig().getString("kit.slots-set")).replace("{kit-name}", kitName).replace("{slot}", args[1]));
    }
}