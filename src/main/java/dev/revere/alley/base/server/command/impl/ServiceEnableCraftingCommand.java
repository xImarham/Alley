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
public class ServiceEnableCraftingCommand extends BaseCommand {
    @CommandData(
            name = "service.enablecrafting",
            description = "Command to re-enable crafting for a specific item.",
            usage = "/service enablecrafting",
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

        if (!serverService.getBlockedCraftingMaterials().contains(itemType)) {
            player.sendMessage(CC.translate("&aThe item you're holding is not blocked from crafting."));
            return;
        }

        serverService.removeFromBlockedCraftingList(itemType);
        serverService.saveCraftingRecipes(itemType);

        player.sendMessage(CC.translate("&aCrafting operations for &6" + itemType.name() + " &ahave been re-enabled."));
    }
}
