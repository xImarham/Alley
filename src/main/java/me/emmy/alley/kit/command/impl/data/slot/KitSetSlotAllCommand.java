package me.emmy.alley.kit.command.impl.data.slot;

import me.emmy.alley.Alley;
import me.emmy.alley.api.command.BaseCommand;
import me.emmy.alley.api.command.Command;
import me.emmy.alley.api.command.CommandArgs;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
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

        if (args.length <1) {
            sender.sendMessage(CC.translate("&cUsage: /kit setslotall (kit-name) (slot)"));
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
        sender.sendMessage(CC.translate(ConfigHandler.getInstance().getMessagesConfig().getString("kit.slots-set")).replace("{kit-name}", kitName).replace("{slot}", args[1]));
    }
}
