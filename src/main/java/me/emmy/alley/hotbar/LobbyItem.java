package me.emmy.alley.hotbar;

import lombok.Getter;
import me.emmy.alley.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author ZionRank
 * @project WavePractice
 * @since 03/12/2023
 */

@Getter
public enum LobbyItem {
    SOLO_GAME("&aSolo Game", Material.IRON_AXE, 0, 0, "sologame"),
    DUO_GAME("&6Duo Game", Material.DIAMOND_AXE, 0, 1, "duogame"),
    LEADERBOARD("&dLeaderboard", Material.EMERALD, 0, 4, "leaderboard"),
    SETTINGS("&cSettings", Material.BOOK, 0, 8, "settings");


    private final String name;
    private final Material type;
    private final int data;
    private final int slot;
    private String command;
    private int menu;

    LobbyItem(String name, Material type, int data, int slot, String command) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.slot = slot;

        this.command = command;
    }

    LobbyItem(String name, Material type, int data, int slot, int menu) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.slot = slot;

        this.menu = menu;
    }

    public static LobbyItem getByItemStack(ItemStack itemStack) {
        for (LobbyItem lobbyItem : values()) {
            if (lobbyItem.constructItem().isSimilar(itemStack)) {
                return lobbyItem;
            }
        }

        return null;
    }

    ItemStack constructItem() {
        return new ItemBuilder(type).name(name).durability(data).build();
    }

}