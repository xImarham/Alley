package me.emmy.alley.leaderboard.menu.button;

import me.emmy.alley.leaderboard.menu.personal.StatisticsMenu;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class StatisticsButton extends Button {

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.PAPER)
                .name("&b&lYour Statistics")
                .lore(
                        "",
                        " &fYour statistics are displayed here.",
                        " &fYou can view your wins, losses, and more.",
                        "",
                        "&fClick to view your statistics."
                )
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }
        playNeutral(player);
        new StatisticsMenu().openMenu(player);
    }
}
