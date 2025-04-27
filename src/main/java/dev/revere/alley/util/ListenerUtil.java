package dev.revere.alley.util;

import dev.revere.alley.Alley;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
@UtilityClass
public class ListenerUtil {
    /**
     * After 5 seconds, clears the dropped items on death via a BukkitRunnable.
     *
     * @param event      The event.
     * @param deadPlayer The dead player.
     */
    public void clearDroppedItemsOnDeath(PlayerDeathEvent event, Player deadPlayer) {
        List<Item> droppedItems = new ArrayList<>();
        for (ItemStack drop : event.getDrops()) {
            if (drop != null && drop.getType() != Material.AIR) {
                droppedItems.add(deadPlayer.getWorld().dropItemNaturally(deadPlayer.getLocation(), drop));
            }
        }
        event.getDrops().clear();

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Item item : droppedItems) {
                    if (item != null && item.isValid()) {
                        item.remove();
                    }
                }
            }
        }.runTaskLater(Alley.getInstance(), 100L);
    }

    /**
     * Checks if the player is not stepping on a pressure plate.
     *
     * @param event The event.
     * @return true if the player is stepping on a pressure plate, false otherwise.
     */
    public boolean notSteppingOnPlate(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) {
            return false;
        }

        Material type = event.getClickedBlock().getType();
        return !pressurePlates.contains(type);
    }

    /**
     * List of pressure plate materials.
     */
    private final List<Material> pressurePlates = Arrays.asList(
            Material.WOOD_PLATE,
            Material.STONE_PLATE,
            Material.IRON_PLATE,
            Material.GOLD_PLATE
    );
}