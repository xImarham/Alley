package dev.revere.alley.feature.layout.menu.button.editor;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.layout.record.LayoutRecord;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 03/05/2025
 */
@AllArgsConstructor
public class LayoutDeleteButton extends Button {
    private final LayoutRecord layout;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.INK_SACK)
                .name("&c&lDelete Layout")
                .durability(1)
                .lore(
                        "&7Warning: Permanent!",
                        "",
                        "&aClick to delete!"
                )
                .hideMeta()
                .build();
    }
}
