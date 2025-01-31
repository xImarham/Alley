package dev.revere.alley.feature.hotbar;

import lombok.Data;
import dev.revere.alley.feature.hotbar.enums.HotbarItems;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Data
public class HotbarItem {
    private final HotbarType hotbarType;
    private final HotbarItems hotbarItems;
    private final ItemStack itemStack;
    private final int slot;
}