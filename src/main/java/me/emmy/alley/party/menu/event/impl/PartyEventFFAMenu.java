package me.emmy.alley.party.menu.event.impl;

import lombok.AllArgsConstructor;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.button.BackButton;
import me.emmy.alley.party.menu.event.PartyEventMenu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 08/10/2024 - 18:39
 */
@AllArgsConstructor
public class PartyEventFFAMenu extends Menu {
    private final BackButton backButton = new BackButton(new PartyEventMenu());

    @Override
    public String getTitle(Player player) {
        return "&c&lStill in development";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, backButton);

        addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}