package dev.revere.alley.feature.kit.command.impl.data.slot;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 00:22
 */
public class KitSetUnrankedSlotCommand extends BaseCommand {
    @CommandData(name = "kit.setunrankedslot", isAdminOnly = true)
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (command.length() < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setunrankedslot &b<kitName> <slot>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate(KitLocale.SLOT_MUST_BE_NUMBER.getMessage()));
            return;
        }

        kit.setUnrankedslot(slot);
        Alley.getInstance().getKitService().saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_UNRANKEDSLOT_SET.getMessage()).replace("{kit-name}", kit.getName()).replace("{slot}", String.valueOf(slot)));
    }
}