package me.emmy.alley.host.impl.tournament.menu;

import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 08/06/2024 - 21:14
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
