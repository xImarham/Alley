package dev.revere.alley.game.match.snapshot.menu.button.items;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotInventoryButton extends Button {
    private final ItemStack item;

    @Override
    public ItemStack getButtonItem(Player player) {
        if (this.item == null) {
            return new ItemStack(Material.AIR);
        }
        return new ItemBuilder(this.item).build();
    }
}
