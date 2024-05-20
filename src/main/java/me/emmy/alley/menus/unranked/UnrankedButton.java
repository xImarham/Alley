package me.emmy.alley.menus.unranked;

import lombok.AllArgsConstructor;
import me.emmy.alley.kit.Kit;
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

    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(kit.getIcon()).name(kit.getDisplayName()).lore(Arrays.asList(
                "&d" + kit.getDescription(),
                "",
                "&fIn Queue: &dnull",
                "&fIn Fights: &dnull",
                "",
                "&dClick to join the &e" + kit.getName() + " &dqueue!")
        ).hideMeta().build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {

        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }

        player.playSound(player.getLocation(), Sound.STEP_GRASS, 2.0F, 1.5F);
    }
}