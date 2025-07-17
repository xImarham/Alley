package dev.revere.alley.game.match.snapshot.menu.button.items;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.match.snapshot.Snapshot;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class SnapshotArmorButton extends Button {
    private final Snapshot snapshot;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.snapshot.getArmor()[this.armorPart]).build();
    }
}