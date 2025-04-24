package dev.revere.alley.feature.kit.command.impl.data.slot;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.command.CommandSender;

/**
 * @author Emmy
 * @project Alley
 * @date 03/10/2024 - 15:55
 */
public class KitSetSlotAllCommand extends BaseCommand {
    @CommandData(name = "kit.setslotall", isAdminOnly = true, inGameOnly = false)
    @Override
    public void onCommand(CommandArgs command) {
        CommandSender sender = command.getSender();
        String[] args = command.getArgs();

        if (args.length < 2) {
            sender.sendMessage(CC.translate("&6Usage: &e/kit setslotall &b<kitName> <slot>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            sender.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.translate(KitLocale.SLOT_MUST_BE_NUMBER.getMessage()));
            return;
        }

        kit.setEditorslot(slot);
        kit.setRankedslot(slot);
        kit.setUnrankedslot(slot);
        Alley.getInstance().getKitService().saveKit(kit);
        sender.sendMessage(CC.translate(KitLocale.KIT_SLOTS_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{slot}", String.valueOf(slot)));
    }
}