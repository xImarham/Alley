package me.emmy.alley.party.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.party.menu.impl.PartyEventFFAMenu;
import me.emmy.alley.party.menu.impl.PartyEventSplitMenu;
import me.emmy.alley.util.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:29
 */
@AllArgsConstructor
public class PartyEventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&b&lChoose a party event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(1, new PartyEventButton(
                Material.DIAMOND_SWORD, 0,
                "&b&lTeam split",
                Arrays.asList(
                        "&fSplit the party into",
                        "&f2 teams and fight",
                        "&fagainst each other.",
                        "",
                        "&bClick to select a kit."
                )
        ));

        buttons.put(4, new PartyEventButton(
                Material.GOLD_AXE, 0,
                "&b&lFree for all",
                Arrays.asList(
                        "&fEvery player fights",
                        "&fagainst each other.",
                        "",
                        "&bClick to select a kit."
                )
        ));

        buttons.put(7, new PartyEventButton(
                Material.REDSTONE, 0,
                "&cBest of 3 Sumo",
                Arrays.asList(
                        "&fThis event is not",
                        "&fimplemented yet.",
                        "",
                        "&c&mClick to start the event."
                )
        ));

        return buttons;
    }

    @AllArgsConstructor
    private static class PartyEventButton extends Button {
        private Material material;
        private int durability;
        private String name;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(name)
                    .lore(lore)
                    .durability(durability)
                    .build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (clickType != ClickType.LEFT) return;

            switch (material) {
                case DIAMOND_SWORD:
                    new PartyEventSplitMenu().openMenu(player);
                    break;
                case GOLD_AXE:
                    new PartyEventFFAMenu().openMenu(player);
                    break;
                case INK_SACK:
                    // Start best of 3 sumo event
                    break;
            }
        }
    }
}