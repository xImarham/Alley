package dev.revere.alley.feature.kit.command.impl.manage;

import dev.revere.alley.Alley;
import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.KitService;
import dev.revere.alley.locale.impl.KitLocale;
import dev.revere.alley.util.visual.ActionBarUtil;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 13:06
 */
public class KitCreateCommand extends BaseCommand {
    @CommandData(name = "kit.create", permission = "alley.admin")
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();
        String[] args = command.getArgs();

        if (args.length < 2) {
            player.sendMessage(CC.translate("&6Usage: &e/kit create &b<kitName> <unranked-slot>"));
            return;
        }

        String kitName = args[0];

        KitService kitService = Alley.getInstance().getKitService();
        if (kitService.getKit(kitName) != null) {
            player.sendMessage(CC.translate(KitLocale.KIT_ALREADY_EXISTS.getMessage()));
            return;
        }

        ItemStack[] inventory = InventoryUtil.cloneItemStackArray(player.getInventory().getContents());
        ItemStack[] armor = InventoryUtil.cloneItemStackArray(player.getInventory().getArmorContents());

        Material icon = Material.DIAMOND_SWORD;
        if (player.getItemInHand() != null && player.getItemInHand().getType() != Material.AIR) {
            icon = player.getItemInHand().getType();
        }

        int slot;
        try {
            boolean slotTaken = false;
            for (Kit kit : kitService.getKits()) {
                if (kit.getUnrankedslot() == Integer.parseInt(args[1])) {
                    player.sendMessage(CC.translate("&cThat slot is already taken by the &7" + kit.getName() + " &ckit!"));
                    slotTaken = true;
                    break;
                }
            }

            if (slotTaken) return;
            slot = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(CC.translate("&cInvalid slot number!"));
            return;
        }

        if (slot < 0) {
            player.sendMessage(CC.translate("&cSlot number cannot be less than 0!"));
            return;
        }

        kitService.createKit(kitName, inventory, armor, icon, slot);
        Alley.getInstance().getProfileService().loadProfiles(); // to update the kits in the database
        ActionBarUtil.sendMessage(player, KitLocale.KIT_CREATED.getMessage().replace("{kit-name}", kitName), 5);

        player.sendMessage(CC.translate(KitLocale.KIT_CREATED.getMessage().replace("{kit-name}", kitName)));
        player.sendMessage(CC.translate("&7Do not forget to reload the queues by using &c&l/queue reload&7."));
    }
}