package me.emmy.alley.queue.menu.queues;

import lombok.AllArgsConstructor;
import me.emmy.alley.utils.SoundUtil;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
public class QueuesButton extends Button {

    private String displayName;
    private Material material;
    private short data;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(material)
                .name(displayName)
                .durability(data)
                .lore(lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        if (material.equals(Material.IRON_SWORD)) {
            player.performCommand("unranked");
            SoundUtil.playSuccess(player);
        } else if (material.equals(Material.DIAMOND_SWORD)) {
            player.performCommand("ranked");
            SoundUtil.playSuccess(player);
        } else if (material.equals(Material.GOLD_AXE)) {
            player.performCommand("ffa");
            SoundUtil.playSuccess(player);
        }
    }
}
