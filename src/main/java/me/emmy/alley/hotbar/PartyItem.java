package me.emmy.alley.hotbar;

import lombok.Getter;
import me.emmy.alley.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@Getter
public enum PartyItem {
    DUO_UNRANKED_QUEUE("&dUnranked Queue", Material.IRON_SWORD, 0, 0, "unrankedduo"),
    KIT_EDITOR("&dKit Editor", Material.BOOK, 0, 2, "kiteditor"),
    START_PARTY_EVENT("&dStart Party Event", Material.IRON_AXE, 0, 4, "party event"),
    FIGHT_OTHER_PARTY("&dFight Other Party", Material.DIAMOND_AXE, 0, 5, "party duel"),
    PARTY_INFO("&dParty Info", Material.PAPER, 0, 7, "party info"),
    PARTY_LEAVE("&cLeave Party", Material.REDSTONE, 0, 8, "party leave");

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
    PartyItem(String name, Material type, int data, int slot, String command) {
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
    PartyItem(String name, Material type, int data, int slot, int menu) {
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
    public static PartyItem getByItemStack(ItemStack itemStack) {
        for (PartyItem partyItem : values()) {
            if (partyItem.constructItem().isSimilar(itemStack)) {
                return partyItem;
            }
        }

        return null;
    }

    public ItemStack constructItem() {
        return new ItemBuilder(type).name(name).durability(data).build();
    }
}