package me.emmy.alley.profile.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.profile.cosmetic.menu.CosmeticsMenu;
import me.emmy.alley.utils.SoundUtil;
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
public class SettingsButton extends Button {

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

        if (material.equals(Material.FEATHER)) {
            player.performCommand("togglepartymessages");
            playNeutral(player);
        } else if (material.equals(Material.NAME_TAG)) {
            player.performCommand("togglepartyinvites");
            playNeutral(player);
        } else if (material.equals(Material.CARPET) && data == (short) 5) {
            player.performCommand("togglescoreboard");
            playNeutral(player);
        } else if (material.equals(Material.ITEM_FRAME)){
            player.performCommand("toggletablist");
            playNeutral(player);
        } else if (material.equals(Material.BOOK)){
            player.performCommand("cosmetics");
            playNeutral(player);
        }
    }
}

