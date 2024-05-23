package me.emmy.alley.menus.ranked;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.menus.unranked.UnrankedButton;
import me.emmy.alley.queue.Queue;
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

@AllArgsConstructor
public class RankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Ranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Queue queue : Alley.getInstance().getQueueRepository().getQueues()) {
            if (queue.getKit().isSettingEnabled(KitSettingRankedImpl.class)) {
                buttons.put(queue.getKit().getRankedslot(), new RankedButton(queue));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}

