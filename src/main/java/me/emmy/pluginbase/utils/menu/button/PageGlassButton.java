package me.emmy.pluginbase.utils.menu.button;

import me.emmy.pluginbase.utils.menu.Button;
import me.emmy.pluginbase.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PageGlassButton extends Button {

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(Material.STAINED_GLASS_PANE)
                .durability(11)
                .build();
    }
}
