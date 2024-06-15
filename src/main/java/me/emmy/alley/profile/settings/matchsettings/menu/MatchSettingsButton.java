package me.emmy.alley.profile.settings.matchsettings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.profile.cosmetic.menu.CosmeticsMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
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
public class MatchSettingsButton extends Button {

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

        switch (material) {
            case FIREWORK:
                new CosmeticsMenu("KillEffect").openMenu(player);
                break;
            case CHEST:
                //matchsetting clear inventory
                break;
            case FEATHER:
                //matchsetting toggle flight
                break;
        }
        playNeutral(player);
    }
}

