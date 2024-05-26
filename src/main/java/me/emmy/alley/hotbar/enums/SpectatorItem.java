package me.emmy.alley.hotbar.enums;

import lombok.Getter;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum SpectatorItem {
    STOP_WATCHING("&cStop Watching", Material.REDSTONE, 0, 8, "leavespectator");

    private final String name;
    private final Material type;
    private final int data;
    private final int slot;
    private final String command;

    /**
     * Constructor for items that run a command.
     *
     * @param name The name of the item.
     * @param type The type of the item.
     * @param data The data of the item.
     * @param slot The slot of the item.
     * @param command The command to run.
     */
    SpectatorItem(String name, Material type, int data, int slot, String command) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.slot = slot;
        this.command = command;
    }

    /**
     * Gets a spectator item by an item stack.
     *
     * @param itemStack The item stack to get the spectator item from.
     * @return The spectator item.
     */
    public static SpectatorItem getByItemStack(ItemStack itemStack) {
        for (SpectatorItem queueItem : values()) {
            if (queueItem.constructItem().isSimilar(itemStack)) {
                return queueItem;
            }
        }

        return null;
    }

    public ItemStack constructItem() {
        return new ItemBuilder(type).name(name).durability(data).build();
    }
}