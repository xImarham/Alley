package dev.revere.alley.feature.layout.menu;

import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.feature.layout.menu.button.LayoutButton;
import dev.revere.alley.feature.layout.menu.button.LayoutModeSwitcherButton;
import dev.revere.alley.feature.queue.Queue;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 02/05/2025
 */
@AllArgsConstructor
public class LayoutMenu extends Menu {
    private EnumKitCategory kitCategory;

    @Override
    public String getTitle(Player player) {
        return "&b&lLayout Editor";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        for (Queue queue : this.plugin.getQueueService().getQueues()) {
            if (!queue.isRanked() && queue.getKit().getCategory() == this.kitCategory) {
                buttons.put(queue.getKit().getEditorSlot(), new LayoutButton(queue.getKit()));
            }
        }

        if (this.kitCategory == EnumKitCategory.NORMAL) {
            buttons.put(40, new LayoutModeSwitcherButton(EnumKitCategory.EXTRA));
        } else {
            buttons.put(4, new LayoutModeSwitcherButton(EnumKitCategory.NORMAL));
        }

        this.addGlass(buttons, (byte) 15);

        return buttons;
    }

    @Override
    public int getSize() {
        if (this.kitCategory == EnumKitCategory.EXTRA) {
            return 9 * 4;
        }

        return 9 * 5;
    }
}