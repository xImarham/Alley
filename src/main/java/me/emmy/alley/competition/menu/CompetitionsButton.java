package me.emmy.alley.competition.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:20
 */
@AllArgsConstructor
public class CompetitionsButton extends Button {

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
    public void clicked(Player player, ClickType clickType) {
        if (clickType != ClickType.LEFT) {
            return;
        }

        switch (itemStack.getType()) {
            case DIAMOND:
                // Open the tournament menu
                break;
            case EMERALD:
                // Open the event menu
                break;
        }

        playNeutral(player);
    }
}
