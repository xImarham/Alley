package dev.revere.alley.feature.kit.command.impl.data.inventory;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.locale.KitLocale;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 22:23
 */
public class KitSetInvCommand extends BaseCommand {
    @CommandData(name = "kit.setinventory", aliases = "kit.setinv", permission = "practice.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 1) {
            player.sendMessage(CC.translate("&6Usage: &e/kit setinventory &b<kitName>"));
            return;
        }

        KitService kitService = this.plugin.getKitService();
        Kit kit = kitService.getKit(args[0]);
        if (kit == null) {
            player.sendMessage(CC.translate(KitLocale.KIT_NOT_FOUND.getMessage()));
            return;
        }

        if (player.getGameMode() == GameMode.CREATIVE) {
            player.sendMessage(CC.translate(KitLocale.KIT_CANNOT_SET_IN_CREATIVE.getMessage()));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        kit.setInventory(inventory);
        kit.setArmor(armor);
        kitService.saveKit(kit);
        player.sendMessage(CC.translate(KitLocale.KIT_INVENTORY_SET.getMessage().replace("{kit-name}", kit.getName())));
    }
}