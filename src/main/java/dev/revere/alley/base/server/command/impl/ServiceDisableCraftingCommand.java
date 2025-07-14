package dev.revere.alley.base.server.command.impl;

import dev.revere.alley.api.command.BaseCommand;
import dev.revere.alley.api.command.CommandArgs;
import dev.revere.alley.api.command.annotation.CommandData;
import dev.revere.alley.base.server.IServerService;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project alley-practice
 * @since 14/07/2025
 */
public class ServiceDisableCraftingCommand extends BaseCommand {
    @CommandData(
            name = "service.disablecrafting",
            description = "Command to manage service crafting operations.",
            usage = "/service disablecrafting <true|false>",
            isAdminOnly = true
    )
    @Override
    public void onCommand(CommandArgs command) {
        Player player = command.getPlayer();

        if (player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType() == Material.AIR) {
            player.sendMessage(CC.translate("&cYou must be holding an item to manage crafting operations."));
            return;
        }

        IServerService serverService = this.plugin.getService(IServerService.class);
        Material itemType = player.getInventory().getItemInHand().getType();
        if (!serverService.isCraftable(itemType)) {
            player.sendMessage(CC.translate("&cThe item you're holding cannot be crafted."));
            return;
        }

        if (serverService.getBlockedCraftingMaterials().contains(itemType)) {
            player.sendMessage(CC.translate("&cThe item you're holding is already blocked from crafting."));
            return;
        }

        serverService.addToBlockedCraftingList(itemType);
        serverService.saveCraftingRecipes(itemType);

        player.sendMessage(CC.translate("&aCrafting operations for &6" + itemType.name() + " &aare now disabled."));
    }
}