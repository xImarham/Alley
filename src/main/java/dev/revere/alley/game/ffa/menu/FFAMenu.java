package dev.revere.alley.game.ffa.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.base.queue.menu.QueuesMenuDefault;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.IFFAService;
import dev.revere.alley.profile.progress.IProgressService;
import lombok.AllArgsConstructor;
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
        return "&6&lSelect a FFA Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        for (AbstractFFAMatch match : Alley.getInstance().getService(IFFAService.class).getMatches()) {
            buttons.put(match.getKit().getFfaSlot(), new FFAButton(match));
        }

        this.addBorder(buttons, 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}