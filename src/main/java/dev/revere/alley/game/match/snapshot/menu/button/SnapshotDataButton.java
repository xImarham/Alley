package dev.revere.alley.game.match.snapshot.menu.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
@AllArgsConstructor
public class SnapshotDataButton extends Button {
    private final String name;
    private final List<String> lore;
    private final Material material;
    private final int durability;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.name)
                .lore(this.lore)
                .durability(this.durability)
                .hideMeta()
                .build();
    }
}