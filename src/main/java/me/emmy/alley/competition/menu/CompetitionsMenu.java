package me.emmy.alley.competition.menu;

import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:19
 */
public class CompetitionsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "Choose an event type";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(12, new CompetitionsButton("&cStart a Tournament", new ItemStack(Material.DIAMOND_SWORD), Arrays.asList(
                "",
                "&fClick to start a &cnew &ftournament.",
                "&fPlayers will be able to &cjoin &fand &ccompete.",
                "",
                " &f* &cSelect a kit",
                " &f* &cChoose if solo or team",
                ""
        )));

        buttons.put(14, new CompetitionsButton("&9Host an Event", new ItemStack(Material.EMPTY_MAP), Arrays.asList(
                "",
                "&fClick to &9choose &fan event type to host",
                "&fand &9create &fa new event for players to join.",
                "",
                " &f* &9Select an event type",
                " &f* &9Choose if solo or team",
                ""
        )));

        addBorder(buttons, (byte) 6, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
