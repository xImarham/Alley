package dev.revere.alley.feature.cosmetic.menu.button;

import dev.revere.alley.feature.cosmetic.menu.CosmeticsMenu;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.api.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public class SoundEffectButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&b&lSound Effects")
                .lore(
                        "&fAll of your sound effects",
                        "&fare displayed here.",
                        "",
                        "&aClick to view your sound effects."
                )
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player    the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }
        playNeutral(player);
        new CosmeticsMenu("SoundEffect").openMenu(player);
    }
}
