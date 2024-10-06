package me.emmy.alley.kit.editor.menu;

import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 21:33
 */
public class EditorSelectMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return Collections.emptyMap();
    }
}
