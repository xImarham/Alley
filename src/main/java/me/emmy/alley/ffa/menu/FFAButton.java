package me.emmy.alley.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.utils.SoundUtil;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:29
 */
@AllArgsConstructor
public class FFAButton extends Button {

    private final AbstractFFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&fPlaying: &b" + match.getPlayers().size() + "/" + match.getMaxPlayers());
        lore.add("&fArena: &b" + match.getArena().getName());
        lore.add("&fKit: &b" + match.getKit().getName());
        lore.add("");
        lore.add("&fClick to join the &b" + match.getName() + " &fqueue.");

        return new ItemBuilder(match.getKit().getIcon())
                .name("&b&l" + match.getName())
                .durability(match.getKit().getIconData())
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;
        SoundUtil.playSuccess(player);
        match.join(player);
    }
}
