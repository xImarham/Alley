package me.emmy.alley.hotbar.enums;

import lombok.Getter;
import me.emmy.alley.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum LobbyItem {
    UNRANKED_QUEUE("&dQueues", Material.IRON_SWORD, 0, 0, "queues"),
    RANKED_QUEUE("&dRanked Queue", Material.DIAMOND_SWORD, 0, 1, "ranked"),
    KIT_EDITOR("&dKit Editor", Material.BOOK, 0, 2, "kiteditor"),
    PARTY("&dCreate Party", Material.NAME_TAG, 0, 4, "party create"),
    LEADERBOARD("&dLeaderboard", Material.EMERALD, 0, 6, "leaderboards"),
    EVENTS("&dEvents", Material.EYE_OF_ENDER, 0, 7, "events"),
    SETTINGS("&dSettings", Material.SKULL_ITEM, 0, 8, "settings");

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
    LobbyItem(String name, Material type, int data, int slot, String command) {
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
    LobbyItem(String name, Material type, int data, int slot, int menu) {
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
    public static LobbyItem getByItemStack(ItemStack itemStack) {
        for (LobbyItem lobbyItem : values()) {
            if (lobbyItem.constructItem().isSimilar(itemStack)) {
                return lobbyItem;
            }
        }

        return null;
    }

    public ItemStack constructItem() {
        return new ItemBuilder(type).name(name).durability(data).build();
    }
}