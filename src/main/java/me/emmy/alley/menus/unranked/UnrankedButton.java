package me.emmy.alley.menus.unranked;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@AllArgsConstructor
public class UnrankedButton extends Button {

    private final Queue queue;

    @Override
    public ItemStack getButtonItem(Player player) {
        Kit kit = queue.getKit();
        return new ItemBuilder(kit.getIcon()).name(kit.getDisplayName()).lore(Arrays.asList(
                "&7" + kit.getDescription(),
                "",
                "&fIn Queue: &d" + queue.getProfiles().size(),
                "&fIn Fights: &dnull",
                "",
                "&fClick to join the &d" + kit.getName() + " &fqueue!")
        ).hideMeta().build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        queue.addPlayer(player);
        PlayerUtil.reset(player);
        player.closeInventory();
        Alley.getInstance().getHotbarUtility().applyQueueItems(player);
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 2.0F, 1.5F);
    }
}