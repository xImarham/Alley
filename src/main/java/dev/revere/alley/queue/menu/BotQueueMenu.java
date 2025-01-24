package dev.revere.alley.queue.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.queue.Queue;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.impl.BackButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 14/09/2024 - 23:58
 */
@AllArgsConstructor
public class BotQueueMenu extends Menu {

    private final Queue queue;

    @Override
    public String getTitle(Player player) {
        return "&b&lBot Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new QueuesMenu()));

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}