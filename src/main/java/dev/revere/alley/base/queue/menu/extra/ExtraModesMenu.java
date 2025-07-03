package dev.revere.alley.base.queue.menu.extra;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.enums.EnumKitCategory;
import dev.revere.alley.base.queue.IQueueService;
import dev.revere.alley.base.queue.Queue;
import dev.revere.alley.base.queue.enums.EnumQueueType;
import dev.revere.alley.base.queue.menu.button.UnrankedButton;
import dev.revere.alley.base.queue.menu.extra.button.QueueModeSwitcherButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 01/05/2025
 */
@AllArgsConstructor
public class ExtraModesMenu extends Menu {
    private final EnumQueueType queueType;

    @Override
    public String getTitle(Player player) {
        return "&6&l" + this.queueType.getMenuTitle();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(4, new QueueModeSwitcherButton(this.queueType, EnumKitCategory.NORMAL));

        int slot = 10;
        for (Queue queue : Alley.getInstance().getService(IQueueService.class).getQueues()) {
            if (shouldAddQueue(queue, queueType)) {
                slot = this.skipIfSlotCrossingBorder(slot);
                buttons.put(slot++, new UnrankedButton(queue));
            }
        }

        this.addGlass(buttons, 15);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }

    private boolean shouldAddQueue(Queue queue, EnumQueueType queueType) {
        if (queue.isRanked() || queue.getKit().getCategory() != EnumKitCategory.EXTRA) {
            return false;
        }

        return (queueType == EnumQueueType.DUOS) == queue.isDuos();
    }
}