package me.emmy.alley.match.snapshot.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.match.snapshot.Snapshot;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.CommandButton;
import me.emmy.alley.utils.menu.button.DisplayButton;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

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