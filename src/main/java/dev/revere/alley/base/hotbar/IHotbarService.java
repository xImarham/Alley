package dev.revere.alley.base.hotbar;

import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.core.lifecycle.IService;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IHotbarService extends IService {
    /**
     * Applies a specific type of hotbar layout to a player's inventory.
     *
     * @param player The player to apply the hotbar to.
     * @param type   The type of hotbar to apply.
     */
    void applyHotbarItems(Player player, EnumHotbarType type);

    /**
     * Determines the correct hotbar type based on the player's current profile state
     * and applies it to their inventory.
     *
     * @param player The player to apply the hotbar to.
     */
    void applyHotbarItems(Player player);

    /**
     * Finds a registered HotbarItem object by its corresponding ItemStack.
     *
     * @param item The ItemStack to search for.
     * @return The HotbarItem, or null if not found.
     */
    HotbarItem getItemByStack(ItemStack item);

    /**
     * Gets a specific HotbarItem by its type and enum definition.
     *
     * @param type       The hotbar type.
     * @param hotbarItem The HotbarItems enum constant.
     * @return The HotbarItem, or null if not found.
     */
    HotbarItem getItemByStack(EnumHotbarType type, HotbarItems hotbarItem);
}
