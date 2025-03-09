package dev.revere.alley.service.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.service.ServerService;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.data.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 09/03/2025
 */
public class ServicePrepareRebootButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.REDSTONE)
                .name("&c&lPrepare Reboot")
                .lore(
                        "&fThis will prepare the server",
                        "&ffor a reboot by cancelling",
                        "&fall tasks and on-going matches.",
                        "",
                        "&cWARNING:",
                        " &fThere won't be a",
                        " &fconfirmation menu.",
                        "",
                        "&cClick to prepare!"
                )
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        if (!player.isOp()) {
            player.sendMessage(CC.translate("&cYou do not have permission to use this button."));
            return;
        }

        Alley plugin = Alley.getInstance();
        ServerService serverService = plugin.getServerService();

        if (!serverService.isAllowQueueing()) {
            player.sendMessage(CC.translate("&cThe server is already preparing for a reboot."));
            return;
        }

        serverService.disbandMatches(player, plugin);
        serverService.disbandParties(player, plugin);
        serverService.removePlayersFromQueue(player, plugin);

        serverService.setAllowQueueing(false);
        Arrays.asList(
                "",
                "&c&lServer is preparing for a reboot...",
                ""
        ).forEach(line -> Bukkit.broadcastMessage(CC.translate(line)));
    }
}