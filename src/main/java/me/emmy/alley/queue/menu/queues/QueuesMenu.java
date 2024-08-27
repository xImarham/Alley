package me.emmy.alley.queue.menu.queues;

import lombok.AllArgsConstructor;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
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
public class QueuesMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Select a Queue";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new QueuesButton("&bUnranked Queue", Material.IRON_SWORD, (short) 0, Arrays.asList(
                "",
                "&fThe unranked queue helps you &bpractice",
                "&fa &bvariety &fof &bgamemodes &fwithout",
                "&bstat changes &fand no &bloss penalty&f!",
                "",
                "&bClick to select a kit!"
        )));

        buttons.put(13, new QueuesButton("&bFFA Arena", Material.GOLD_AXE, (short) 0, Arrays.asList(
                "",
                "&fFree For All Arena allows you to fight",
                "&ffree for yourself against",
                "&beverybody &felse!",
                "",
                "&bClick to select a kit!"
        )));

        buttons.put(15, new QueuesButton("&bRanked Queue", Material.DIAMOND_SWORD, (short) 0, Arrays.asList(
                "",
                "&fThe ranked queue &bchallenge &fother players",
                "&fand climb the &bleaderboards",
                "&fto become the &bbest&f!",
                "",
                "&bClick to select a kit!"
        )));

        addGlass(buttons, (byte) 15);

        /*
        for (int slot = 0; slot < getSize(); slot++) {
            if (!buttons.containsKey(slot)) {
                buttons.put(slot, new QueuesButton("", Material.STAINED_GLASS_PANE, (short) 15, Arrays.asList("")));
            }
        }
        */

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
