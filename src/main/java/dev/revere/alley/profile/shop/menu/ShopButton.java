package dev.revere.alley.profile.shop.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.profile.shop.menu.impl.ShopEffectMenu;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.util.data.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class ShopButton extends Button {

    private String displayName;
    private ItemStack itemStack;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(itemStack)
                .name(displayName)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        Material material = itemStack.getType();

        switch (material) {
            case FEATHER:
                new ShopEffectMenu("SoundEffect").openMenu(player);
                break;
            case REDSTONE:
                new ShopEffectMenu("KillEffect").openMenu(player);
                break;
        }
        playNeutral(player);
    }
}
