package me.emmy.alley.game.match.snapshot.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class InventorySnapshotMenu extends Menu {

    private final Player target;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&e" + target.getName() + "'s Inventory");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }
}