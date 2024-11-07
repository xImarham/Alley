package dev.revere.alley.game.event.menu;

import dev.revere.alley.api.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:14
 */
public class EventButton extends Button {
    @Override
    public ItemStack getButtonItem(Player player) {
        return null;
    }
}