package me.emmy.alley.game.event.menu;

import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:14
 */
public class EventMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Collections.emptyMap();
    }
}
