package dev.revere.alley.base.kit.command.impl.data.slot;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.KitService;
import dev.revere.alley.config.locale.impl.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 08/06/2025
 */
public class KitSetExtraSlotCommand extends BaseCommand {
    @CommandData(name = "kit.setextraslot", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setextraslot &b<kitName> <slot>"));
            return;
        }

        String kitName = args[0];
        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(kitName);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid slot number! Please enter a valid integer."));
            return;
        }

        kit.setExtraSlot(slot);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate("&aSuccessfully set the extra slot for the &b" + kit.getName() + " &akit to &b" + slot + "&a!"));
    }
}