package me.emmy.alley.ffa.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.ffa.AbstractFFAMatch;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.leaderboard.menu.personal.button.KitStatButton;
import me.emmy.alley.queue.menu.queues.QueuesMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
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
        return "Select a FFA Queue";
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

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
