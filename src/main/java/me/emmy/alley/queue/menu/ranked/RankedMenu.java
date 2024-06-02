package me.emmy.alley.queue.menu.ranked;

import me.emmy.alley.Alley;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:28
 */

public class RankedMenu extends Menu {

    //private QueuesMenu previousMenu;

    @Override
    public String getTitle(Player player) {
        return "Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        //buttons.put(0, new BackButton(previousMenu));

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.isRanked()) {
                buttons.put(queue.getKit().getRankedslot(), new RankedButton(queue));
            }
        }

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}

