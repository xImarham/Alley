package dev.revere.alley.game.host;

import lombok.AllArgsConstructor;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.pagination.ItemBuilder;
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
 * @date 08/06/2024 - 21:19
 */
public class HostMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Choose an event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new HostButton("&cStart a Tournament", new ItemStack(Material.DIAMOND_SWORD), Arrays.asList(
                "",
                "&fClick to start a &cnew &ftournament.",
                "&fPlayers will be able to &cjoin &fand &ccompete.",
                "",
                " &f* &cSelect a kit",
                " &f* &cChoose if solo or team",
                ""
        )));

        buttons.put(14, new HostButton("&9Host an Event", new ItemStack(Material.EMPTY_MAP), Arrays.asList(
                "",
                "&fClick to &9choose &fan event type to host",
                "&fand &9create &fa new event for players to join.",
                "",
                " &f* &9Select an event type",
                " &f* &9Choose if solo or team",
                ""
        )));

        addGlass(buttons, (byte) 15);

        return buttons;
    }

    @AllArgsConstructor
    public static class HostButton extends Button {
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


    @Override
    public int getSize() {
        return 9 * 3;
    }
}
