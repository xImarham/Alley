package dev.revere.alley.game.ffa.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.SoundUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

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
        return new ItemBuilder(this.match.getKit().getIcon())
                .name("&b&l" + this.match.getName())
                .durability(this.match.getKit().getDurability())
                .lore(Arrays.asList(
                        "",
                        "&fPlaying: &b" + this.match.getPlayers().size() + "/" + this.match.getMaxPlayers(),
                        "&fArena: &b" + this.match.getArena().getName(),
                        "&fKit: &b" + this.match.getKit().getName(),
                        "",
                        "&fClick to join the &b" + this.match.getName() + " &fqueue.")
                )
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType != ClickType.LEFT) return;
        SoundUtil.playSuccess(player);
        this.match.join(player);
    }
}