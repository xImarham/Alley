package me.emmy.alley.game.match.snapshot.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.pagination.ItemBuilder;
import me.emmy.alley.game.match.snapshot.Snapshot;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024
 */
@AllArgsConstructor
public class InventorySnapshotArmorButton extends Button {
    private final UUID target;
    private int armorPart;

    @Override
    public ItemStack getButtonItem(Player player) {
        Snapshot snapshot = Alley.getInstance().getSnapshotRepository().getSnapshot(target);
        return new ItemBuilder(snapshot.getArmor()[armorPart])
                .build();
    }
}
