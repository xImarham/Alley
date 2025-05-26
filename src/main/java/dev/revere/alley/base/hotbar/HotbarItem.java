package dev.revere.alley.base.hotbar;

import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Data
public class HotbarItem {
    private final EnumHotbarType hotbarType;
    private final HotbarItems hotbarItems;
    private final ItemStack itemStack;
    private final int slot;
}