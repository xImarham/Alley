package dev.revere.alley.feature.queue.menu.sub;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.feature.queue.enums.EnumQueueType;
import dev.revere.alley.feature.queue.menu.QueuesMenuDefault;
import dev.revere.alley.feature.queue.menu.button.UnrankedButton;
import dev.revere.alley.feature.queue.menu.extra.button.QueueModeSwitcherButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 10:28
 */
public class UnrankedMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lUnranked Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenuDefault()));

        for (Queue queue : Alley.getInstance().getQueueService().getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == EnumKitCategory.NORMAL) {
                buttons.put(queue.getKit().getUnrankedSlot(), new UnrankedButton(queue));
            }
        }

        buttons.put(40, new QueueModeSwitcherButton(EnumQueueType.UNRANKED, EnumKitCategory.EXTRA));

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}