package me.emmy.alley.profile.division.menu;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
public class DivisionsMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&8Divisions Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getDivisionRepository().getDivisions().forEach(division -> {
            buttons.put(division.getSlot(), new DivisionButton(division));
        });

        addBorder(buttons, (byte) 6);

        return buttons;
    }
}
