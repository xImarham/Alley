package dev.revere.alley.base.queue.menu.sub;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.menu.button.RankedButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:28
 */
public class RankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lSolo Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        //buttons.put(0, new BackButton(new QueuesMenu()));

        for (Queue queue : Alley.getInstance().getQueueService().getQueues()) {
            if (queue.isRanked()) {
                buttons.put(queue.getKit().getRankedSlot(), new RankedButton(queue));
            }
        }

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}