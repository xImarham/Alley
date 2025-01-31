package dev.revere.alley.game.ffa.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.feature.queue.menu.QueuesMenu;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
@AllArgsConstructor
public class FFAMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lSelect a FFA Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenu()));

        int slot = 10;

        for (AbstractFFAMatch match : Alley.getInstance().getFfaRepository().getMatches()) {
            buttons.put(slot, new FFAButton(match));
            slot++;
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}