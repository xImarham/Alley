package me.emmy.alley.arena;

import me.emmy.alley.config.ConfigHandler;

import me.emmy.alley.util.chat.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 16:42
 */
@Getter
@Setter
public class Arena {
    private String name;
    private String displayName;
    private Location pos1;
    private Location pos2;
    private Location center;
    private Location minimum;
    private Location maximum;
    private ArenaType type;
    private List<String> kits = new ArrayList<>();
    private boolean enabled;

    /**
     * Constructor for the Arena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public Arena(String name, Location minimum, Location maximum) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public void createArena() {}
    public void saveArena() {}
    public void deleteArena() {}

    /**
     * Clones the arena to the arena world at the specified target location.
     *
     * @param targetLocation The target location in the arena world where the arena will be cloned.
     */
    public void cloneArena(Location targetLocation) {
        String arenaWorldName = ConfigHandler.getInstance().getSettingsConfig().getString("arena-world");
        World arenaWorld = Bukkit.getWorld(arenaWorldName);

        if (arenaWorld == null) {
            Logger.logError("Arena world not found: " + arenaWorldName);
            return;
        }

        int xSize = this.maximum.getBlockX() - this.minimum.getBlockX();
        int ySize = this.maximum.getBlockY() - this.minimum.getBlockY();
        int zSize = this.maximum.getBlockZ() - this.minimum.getBlockZ();

        for (int x = 0; x <= xSize; x++) {
            for (int y = 0; y <= ySize; y++) {
                for (int z = 0; z <= zSize; z++) {
                    Block originalBlock = this.minimum.getWorld().getBlockAt(
                            this.minimum.getBlockX() + x,
                            this.minimum.getBlockY() + y,
                            this.minimum.getBlockZ() + z
                    );

                    Block targetBlock = arenaWorld.getBlockAt(
                            targetLocation.getBlockX() + x,
                            targetLocation.getBlockY() + y,
                            targetLocation.getBlockZ() + z
                    );

                    targetBlock.setType(originalBlock.getType());
                    targetBlock.setData(originalBlock.getData());
                }
            }
        }
    }

    /**
     * Deletes the cloned arena at the specified location.
     *
     * @param clonedArenaLocation The location of the cloned arena to delete.
     */
    public void deleteClonedArena(Location clonedArenaLocation) {
        String arenaWorldName = ConfigHandler.getInstance().getSettingsConfig().getString("arena-world");
        World arenaWorld = Bukkit.getWorld(arenaWorldName);

        if (arenaWorld == null) {
            Logger.logError("Arena world not found: " + arenaWorldName);
            return;
        }

        int xSize = this.maximum.getBlockX() - this.minimum.getBlockX();
        int ySize = this.maximum.getBlockY() - this.minimum.getBlockY();
        int zSize = this.maximum.getBlockZ() - this.minimum.getBlockZ();

        for (int x = 0; x <= xSize; x++) {
            for (int y = 0; y <= ySize; y++) {
                for (int z = 0; z <= zSize; z++) {
                    Block block = arenaWorld.getBlockAt(
                            clonedArenaLocation.getBlockX() + x,
                            clonedArenaLocation.getBlockY() + y,
                            clonedArenaLocation.getBlockZ() + z
                    );

                    block.setType(Material.AIR);
                }
            }
        }
    }
}
