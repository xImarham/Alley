package dev.revere.alley.game.tournament.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/06/2024 - 21:14
 */
public class TournamentMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Collections.emptyMap();
    }
}
