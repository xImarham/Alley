package dev.revere.alley.feature.hotbar;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class HotbarService {
    private final Map<EnumHotbarType, List<HotbarItem>> hotbarItemsByType;

    public HotbarService() {
        this.hotbarItemsByType = new EnumMap<>(EnumHotbarType.class);
        Arrays.stream(EnumHotbarType.values()).forEach(type -> this.hotbarItemsByType.put(type, new ArrayList<>()));
        this.initializeHotbarItems();
    }

    /**
     * Initialize the hotbar items.
     */
    private void initializeHotbarItems() {
        Arrays.stream(HotbarItems.values()).forEach(item -> {
            Set<EnumHotbarType> types = item.getTypes();
            if (types != null) {
                types.forEach(type -> this.addHotbarItem(type, item));
            }
        });
    }

    /**
     * Add a hotbar item to the hotbar repository
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     */
    public void addHotbarItem(EnumHotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> items = this.hotbarItemsByType.computeIfAbsent(type, k -> new ArrayList<>());
        items.add(this.createHotbarItem(type, hotbarItem));
    }

    /**
     * Apply the hotbar items to the player
     *
     * @param player the player
     * @param type   the hotbar type
     */
    public void applyHotbarItems(Player player, EnumHotbarType type) {
        player.getInventory().clear();
        for (HotbarItem item : this.hotbarItemsByType.get(type)) {
            ItemStack itemStack = item.getItemStack();
            if (item.getHotbarItems() == HotbarItems.SETTINGS && itemStack.getDurability() == 3) {
                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                meta.setOwner(player.getName());
                itemStack.setItemMeta(meta);
            }
            itemStack.setDurability((short) item.getHotbarItems().getDurability());
            player.getInventory().setItem(item.getSlot(), itemStack);
        }

        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), player::updateInventory, 1L);
    }

    /**
     * Create a hotbar item with the given type and hotbar item
     *
     * @param type       the hotbar type
     * @param hotbarItem the hotbar item
     * @return the hotbar item
     */
    private HotbarItem createHotbarItem(EnumHotbarType type, HotbarItems hotbarItem) {
        return new HotbarItem(type, hotbarItem,
                new ItemBuilder(hotbarItem.getMaterial())
                        .name(hotbarItem.getName())
                        .hideMeta()
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
        return this.hotbarItemsByType.values().stream()
                .flatMap(List::stream)
                .filter(i -> i.getItemStack().isSimilar(item))
                //.filter(i -> i.getItemStack().equals(item))
                //.filter(i -> i.getItemStack().getDurability() == item.getDurability())
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
    public HotbarItem getItemByStack(EnumHotbarType type, HotbarItems hotbarItem) {
        List<HotbarItem> itemsOfType = this.hotbarItemsByType.get(type);
        if (itemsOfType != null) {
            return itemsOfType.stream()
                    .filter(i -> i.getHotbarItems() == hotbarItem)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}