package me.emmy.alley.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import me.emmy.alley.utils.SoundUtil;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class FFAButton extends Button {

    private final AbstractFFAMatch match;

    @Override
    public ItemStack getButtonItem(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("&fPlaying: &d" + match.getPlayers().size() + "/" + match.getMaxPlayers());
        lore.add("&fArena: &d" + match.getArena().getName());
        lore.add("&fKit: &d" + match.getKit().getName());
        lore.add("");
        lore.add("&fClick to join the &d" + match.getName() + " &fqueue.");

        return new ItemBuilder(match.getKit().getIcon())
                .name("&d&l" + match.getName())
                .durability(match.getKit().getIconData())
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }
        SoundUtil.playSuccess(player);
        match.join(player);
    }
}
