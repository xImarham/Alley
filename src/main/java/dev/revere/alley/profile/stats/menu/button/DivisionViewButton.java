package dev.revere.alley.profile.stats.menu.button;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.division.menu.DivisionsMenu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@AllArgsConstructor
public class DivisionViewButton extends Button {

    //TODO: when implementing global levels, profile field is gonna be required to get the level and so on...

    private final Profile profile;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.DIAMOND)
            .name("&b&lDivisions")
            .lore(
                "&aClick to view your division progress."
            )
            .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        new DivisionsMenu().openMenu(player);
    }
}