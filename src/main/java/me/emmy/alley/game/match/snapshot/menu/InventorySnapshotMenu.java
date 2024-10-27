package me.emmy.alley.game.match.snapshot.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.game.match.snapshot.Snapshot;
import me.emmy.alley.game.match.snapshot.menu.button.InventorySnapshotArmorButton;
import me.emmy.alley.game.match.snapshot.menu.button.ViewOpponentButton;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.item.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024 - 21:16
 */
@AllArgsConstructor
public class InventorySnapshotMenu extends Menu {
    private final UUID target;

    @Override
    public String getTitle(Player player) {
        return CC.translate("&b&l" + Bukkit.getPlayer(target).getName() + "'s Inventory");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        ItemStack[] inventory = Alley.getInstance().getSnapshotRepository().getSnapshot(target).getInventory();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            buttons.put(i, new SnapshotButton(item));
        }

        buttons.put(53, new ViewOpponentButton(Alley.getInstance().getSnapshotRepository().getSnapshot(target).getOpponent()));
        buttons.put(36, new InventorySnapshotArmorButton(target, 0));
        buttons.put(37, new InventorySnapshotArmorButton(target, 1));
        buttons.put(38, new InventorySnapshotArmorButton(target, 2));
        buttons.put(39, new InventorySnapshotArmorButton(target, 3));

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 6;
    }

    @AllArgsConstructor
    private static class SnapshotButton extends Button {
        private final ItemStack item;

        @Override
        public ItemStack getButtonItem(Player player) {
            if (item == null) {
                return new ItemStack(Material.AIR);
            }
            return new ItemBuilder(item).build();
        }
    }
}