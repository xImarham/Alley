package me.emmy.alley.profile.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class ProfileButton extends Button {

    private String displayName;
    private Material material;
    private short data;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(displayName)
                .durability(data)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        if (material.equals(Material.PAPER)) {
            player.performCommand("stats");
            playNeutral(player);
        } else if (material.equals(Material.BOOK)) {
            player.performCommand("matchhistory");
            playNeutral(player);
        } else if (material.equals(Material.SKULL_ITEM)) {
            playNeutral(player);
        } else if (material.equals(Material.ANVIL)) {
            player.performCommand("settings");
            playNeutral(player);
        } else if (material.equals(Material.FEATHER)) {
            player.performCommand("divisions");
            playNeutral(player);
        } else if (material.equals(Material.BEACON)) {
            player.performCommand("cosmetics");
            playNeutral(player);
        } else if (material.equals(Material.ENDER_CHEST)) {
            player.performCommand("themes");
            playNeutral(player);
        } else if (material.equals(Material.EYE_OF_ENDER)) {
            player.performCommand("leaderboards");
            playNeutral(player);
        } else if (material.equals(Material.EMERALD)) {
            player.performCommand("coinshop");
            playNeutral(player);
        }
    }
}

