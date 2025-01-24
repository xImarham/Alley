package dev.revere.alley.game.party.menu.event.impl.ffa;

import lombok.AllArgsConstructor;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.game.party.menu.event.PartyEventMenu;
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

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }
}