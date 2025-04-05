package dev.revere.alley.game.match.snapshot.menu.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.match.snapshot.menu.InventorySnapshotMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class ViewOpponentButton extends Button {
    private final UUID opponent;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name(CC.translate("&bView Opponent"))
                .lore("&7Click to view &b" + Bukkit.getOfflinePlayer(opponent).getName() + "'s &7inventory.")
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) return;

        new InventorySnapshotMenu(opponent).openMenu(player);
    }
}
