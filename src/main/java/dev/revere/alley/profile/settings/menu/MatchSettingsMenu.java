package dev.revere.alley.profile.settings.menu;

import dev.revere.alley.api.menu.impl.BackButton;
import lombok.AllArgsConstructor;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class MatchSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lMatch Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new PracticeSettingsMenu()));

        buttons.put(10, new MatchSettingsButton("&b&lClear inventory", Material.CHEST, (short) 0, Arrays.asList(
                "&fClear your inventory",
                "&fupon &bend &fof a match.",
                "",
                " &f● &bStatus: &cNULL",
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new MatchSettingsButton("&b&lToggle Flight", Material.FEATHER, (short) 0, Arrays.asList(
                "&fStart &bflying &fupon",
                "&fend of a match.",
                "",
                " &f● &bStatus: &cNULL",
                "",
                "&aClick to toggle!"
        )));

        addBorder(buttons, (byte) 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @AllArgsConstructor
    private static class MatchSettingsButton extends Button {

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

            switch (material) {
                case CHEST:
                    //matchsetting clear inventory
                    break;
                case FEATHER:
                    //matchsetting toggle flight
                    break;
            }
            playNeutral(player);
        }
    }
}