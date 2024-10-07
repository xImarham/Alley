package me.emmy.alley.arena.restoration;

import lombok.experimental.UtilityClass;
import me.emmy.alley.arena.Arena;
import org.bukkit.Chunk;
import org.bukkit.Location;
import me.emmy.alley.arena.ArenaRepository;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 07/10/2024 - 21:20
 */
public class ArenaRestoration {
    private final List<BlockState> addedBlocks = new ArrayList<>();
    private final List<BlockState> removedBlocks = new ArrayList<>();
    private final Location max;
    private final Location min;

    /**
     * Constructor for ArenaRestoration.
     *
     * @param arena The arena to be restored.
     */
    public ArenaRestoration(Arena arena) {
        this.max = arena.getMinimum();
        this.min = arena.getMaximum();
    }

    /**
     * Adds a block to the list of added blocks.
     *
     * @param block The block to be added.
     */
    public void trackAddedBlock(Block block) {
        addedBlocks.add(block.getState());
    }

    /**
     * Adds a block to the list of removed blocks.
     *
     * @param block The block to be removed.
     */
    public void trackRemovedBlock(Block block) {
        removedBlocks.add(block.getState());
    }

    /**
     * Restores the arena to its original state.
     */
    public void restoreArena() {
        for (BlockState state : addedBlocks) {
            state.update(true, false);
        }
        for (BlockState state : removedBlocks) {
            state.getBlock().setType(state.getType());
            state.update(true, false);
        }
        addedBlocks.clear();
        removedBlocks.clear();
    }

    /**
     * Checks if a block is within the arena bounds.
     *
     * @param block The block to check.
     * @return True if the block is within bounds, false otherwise.
     */
    public boolean isWithinBounds(Block block) {
        Location loc = block.getLocation();
        return loc.getX() >= max.getX() && loc.getX() <= min.getX()
                && loc.getY() >= max.getY() && loc.getY() <= min.getY()
                && loc.getZ() >= max.getZ() && loc.getZ() <= min.getZ();
    }
}