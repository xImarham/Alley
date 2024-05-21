package me.emmy.alley.hotbar;

import lombok.Getter;
import me.emmy.alley.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum HotbarItem {
    UNRANKED_QUEUE("&dUnranked Queue", Material.IRON_SWORD, 0, 0, "unranked"),
    RANKED_QUEUE("&dRanked Queue", Material.DIAMOND_SWORD, 0, 1, "ranked"),
    LEADERBOARD("&dLeaderboard", Material.EMERALD, 0, 4, "leaderboards"),
    EVENTS("&dEvents", Material.EYE_OF_ENDER, 0, 7, "events"),
    SETTINGS("&dSettings", Material.BOOK, 0, 8, "settings"),
    LEAVE_QUEUE("&cLeave Queue", Material.IRON_SWORD, 0, 0, null);

    private final String name;
    private final Material type;
    private final int data;
    private final int slot;
    private String command;
    private int menu;

    /**
     * Constructor for items that run a command.
     *
     * @param name The name of the item.
     * @param type The type of the item.
     * @param data The data of the item.
     * @param slot The slot of the item.
     * @param command The command to run.
     */
    HotbarItem(String name, Material type, int data, int slot, String command) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.slot = slot;
        this.command = command;
    }

    /**
     * Constructor for items that open a menu.
     *
     * @param name The name of the item.
     * @param type The type of the item.
     * @param data The data of the item.
     * @param slot The slot of the item.
     * @param menu The menu ID to open.
     */
    HotbarItem(String name, Material type, int data, int slot, int menu) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.slot = slot;
        this.menu = menu;
    }

    /**
     * Gets a lobby item by an item stack.
     *
     * @param itemStack The item stack to get the lobby item from.
     * @return The lobby item.
     */
    public static HotbarItem getByItemStack(ItemStack itemStack) {
        for (HotbarItem hotbarItem : values()) {
            if (hotbarItem.constructItem().isSimilar(itemStack)) {
                return hotbarItem;
            }
        }

        return null;
    }

    public ItemStack constructItem() {
        return new ItemBuilder(type).name(name).durability(data).build();
    }
}