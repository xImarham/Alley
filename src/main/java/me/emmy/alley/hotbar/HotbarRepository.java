package me.emmy.alley.hotbar;

import lombok.Getter;
import me.emmy.alley.hotbar.enums.HotbarItems;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.util.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class HotbarRepository {

    private final Map<HotbarType, List<HotbarItem>> hotbarItemsByType;

    /**
     * Create a new hotbar repository
     */
    public HotbarRepository() {
        this.hotbarItemsByType = new EnumMap<>(HotbarType.class);
        Arrays.stream(HotbarType.values()).forEach(type -> hotbarItemsByType.put(type, new ArrayList<>()));
        initializeHotbarItems();
    }

    /**
     * Initialize the hotbar items
     */
    private void initializeHotbarItems() {
        Arrays.stream(HotbarItems.values()).forEach(item -> {
            Set<HotbarType> types = item.getTypes();
            if (types != null) {
                types.forEach(type -> addHotbarItem(type, item));
            }
        });
    }

    /**
     * Add a hotbar item to the hotbar repository
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     */
    public void addHotbarItem(HotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> items = hotbarItemsByType.computeIfAbsent(type, k -> new ArrayList<>());
        items.add(createHotbarItem(type, hotbarItem));
    }

    /**
     * Apply the hotbar items to the player
     *
     * @param player the player
     * @param type   the hotbar type
     */
    public void applyHotbarItems(Player player, HotbarType type) {
        player.getInventory().clear();
        for (HotbarItem item : hotbarItemsByType.get(type)) {
            player.getInventory().setItem(item.getSlot(), item.getItemStack());
        }
    }

    /**
     * Create a hotbar item with the given type and hotbar item
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     * @return the hotbar item
     */
    private HotbarItem createHotbarItem(HotbarType type, HotbarItems hotbarItem) {
        return new HotbarItem(type, hotbarItem,
                new ItemBuilder(hotbarItem.getMaterial())
                        .name(hotbarItem.getName())
                        .build(),
                hotbarItem.getSlot());
    }

    /**
     * Get a hotbar item by the item stack
     *
     * @param item the item stack
     * @return the hotbar item
     */
    public HotbarItem getItemByStack(ItemStack item) {
        return hotbarItemsByType.values().stream()
                .flatMap(List::stream)
                .filter(i -> i.getItemStack().isSimilar(item))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a hotbar item by the hotbar type and hotbar item
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     * @return the hotbar item
     */
    public HotbarItem getItemByStack(HotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> itemsOfType = hotbarItemsByType.get(type);
        if (itemsOfType != null) {
            return itemsOfType.stream()
                    .filter(i -> i.getHotbarItems() == hotbarItem)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}