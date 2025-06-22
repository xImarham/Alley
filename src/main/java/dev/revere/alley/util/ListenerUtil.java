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
     * After 5 seconds, clears the dropped items on regular item drop via a BukkitRunnable.
     *
     * @param item The dropped item.
     */
    public void clearDroppedItemsOnRegularItemDrop(Item item) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (item != null && item.isValid()) {
                    item.remove();
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

    /**
     * Checks if the material is a door or gate.
     *
     * @param material The material to check.
     * @return true if the material is a door or gate, false otherwise.
     */
    public boolean isDoorOrGate(Material material) {
        return doors.contains(material);
    }

    /**
     * List of door and gate materials.
     */
    private final List<Material> doors = Arrays.asList(
            Material.WOODEN_DOOR,
            Material.SPRUCE_DOOR,
            Material.BIRCH_DOOR,
            Material.JUNGLE_DOOR,
            Material.ACACIA_DOOR,
            Material.DARK_OAK_DOOR,

            Material.FENCE_GATE,
            Material.SPRUCE_FENCE_GATE,
            Material.BIRCH_FENCE_GATE,
            Material.JUNGLE_FENCE_GATE,
            Material.ACACIA_FENCE_GATE,
            Material.DARK_OAK_FENCE_GATE,

            Material.TRAP_DOOR,
            Material.IRON_TRAPDOOR
    );

    /**
     * Checks if the material is a bed fight protected block.
     *
     * @param material The material to check.
     * @return true if the material is a bed fight protected block, false otherwise.
     */
    public boolean isBedFightProtectedBlock(Material material) {
        return bedFightProtectedBlocks.contains(material);
    }

    /**
     * List of bed fight protected block materials.
     */
    private final List<Material> bedFightProtectedBlocks = Arrays.asList(
            Material.ENDER_STONE,
            Material.WOOD,
            Material.WOOL,
            Material.BED_BLOCK
    );

    /**
     * Checks if the material is a bed fight protected block.
     *
     * @param material The material to check.
     * @return true if the material is a bed fight protected block, false otherwise.
     */
    public boolean isSword(Material material) {
        return swords.contains(material);
    }

    /**
     * List of bed fight protected block materials.
     */
    private final List<Material> swords = Arrays.asList(
            Material.DIAMOND_SWORD,
            Material.GOLD_SWORD,
            Material.IRON_SWORD,
            Material.STONE_SWORD,
            Material.WOOD_SWORD
    );
}