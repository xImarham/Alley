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
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:28
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

        buttons.put(11, new QueuesButton("&dUnranked Queue", Material.IRON_SWORD, (short) 0, Arrays.asList(
                "",
                "&fThe unranked queue helps you &dpractice",
                "&fa &dvariety &fof &dgamemodes &fwithout",
                "&dstat changes &fand no &dloss penalty&f!",
                "",
                "&dClick to select a kit!"
        )));

        buttons.put(13, new QueuesButton("&dFFA Arena", Material.GOLD_AXE, (short) 0, Arrays.asList(
                "",
                "&fFree For All Arena allows you to fight",
                "&ffree for yourself against",
                "&deverybody &felse!",
                "",
                "&dClick to select a kit!"
        )));

        buttons.put(15, new QueuesButton("&dRanked Queue", Material.DIAMOND_SWORD, (short) 0, Arrays.asList(
                "",
                "&fThe ranked queue &dchallenge &fother players",
                "&fand climb the &dleaderboards",
                "&fto become the &dbest&f!",
                "",
                "&dClick to select a kit!"
        )));

        for (int slot = 0; slot < getSize(); slot++) {
            if (!buttons.containsKey(slot)) {
                buttons.put(slot, new QueuesButton("", Material.STAINED_GLASS_PANE, (short) 6, Arrays.asList(
                        "",
                        ""
                )));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}
