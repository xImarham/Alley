package dev.revere.alley.util;

import com.boydti.fawe.util.SetQueue;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockID;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/06/2025
 */
public class FAWEArenaManager {
    public void deleteCopiedArena(Location min, Location max, Runnable callback) {
        if (min.getWorld() == null) {
            if (callback != null) callback.run();
            return;
        }

        World world = getWEWorld(min.getWorld());
        if (world == null) {
            if (callback != null) callback.run();
            return;
        }

        Vector minVec = new Vector(
                min.getBlockX(),
                min.getBlockY(),
                min.getBlockZ()
        );

        Vector maxVec = new Vector(
                max.getBlockX(),
                max.getBlockY(),
                max.getBlockZ()
        );

        CuboidRegion region = new CuboidRegion(world, minVec, maxVec);

        long blockCount = region.getArea();
        Bukkit.broadcastMessage("[FAWE] Deleting " + blockCount + " blocks from " + minVec + " to " + maxVec);

        try {
            SetQueue.IMP.addTask(() -> {
                try {
                    long startTime = System.currentTimeMillis();
                    BaseBlock airBlock = new BaseBlock(BlockID.AIR, 0);

                    int processedBlocks = 0;
                    for (Vector vector : region) {
                        world.setBlock(vector, airBlock);
                        processedBlocks++;

                        // Progress logging for large operations
                        if (processedBlocks % 10000 == 0) {
                            Bukkit.broadcastMessage("[FAWE] Processed " + processedBlocks + "/" + blockCount + " blocks");
                        }
                    }

                    long endTime = System.currentTimeMillis();
                    Bukkit.broadcastMessage("[FAWE] Deletion completed in " + (endTime - startTime) + "ms");

                    if (callback != null) {
                        callback.run();
                    }
                } catch (Exception e) {
                    Bukkit.broadcastMessage("[FAWE] Error during deletion:");
                    e.printStackTrace();
                    if (callback != null) callback.run();
                }
            });
        } catch (Exception e) {
            Bukkit.broadcastMessage("[FAWE] Error scheduling deletion task:");
            e.printStackTrace();
            if (callback != null) callback.run();
        }
    }

    public Clipboard createClipboard(Location originalMin, Location originalMax) {
        if (originalMin.getWorld() == null) {
            Bukkit.broadcastMessage("[FAWE] World is null for clipboard creation");
            return null;
        }

        World world = getWEWorld(originalMin.getWorld());
        if (world == null) {
            Bukkit.broadcastMessage("[FAWE] Failed to get WorldEdit world for clipboard");
            return null;
        }

        Vector minVec = new Vector(
                originalMin.getBlockX(),
                originalMin.getBlockY(),
                originalMin.getBlockZ()
        );

        Vector maxVec = new Vector(
                originalMax.getBlockX(),
                originalMax.getBlockY(),
                originalMax.getBlockZ()
        );

        CuboidRegion region = new CuboidRegion(world, minVec, maxVec);

        long blockCount = region.getArea();
        Bukkit.broadcastMessage("[FAWE] Creating clipboard for " + blockCount + " blocks from " + minVec + " to " + maxVec);

        try {
            long startTime = System.currentTimeMillis();
            Clipboard clipboard = new BlockArrayClipboard(region);
            clipboard.setOrigin(minVec);

            ForwardExtentCopy copy = new ForwardExtentCopy(world, region, clipboard, minVec);
            copy.setRemovingEntities(true);

            Operations.complete(copy);

            long endTime = System.currentTimeMillis();
            Bukkit.broadcastMessage("[FAWE] Clipboard created successfully in " + (endTime - startTime) + "ms");

            int nonAirBlocks = 0;
            int totalChecked = 0;
            for (Vector vec : region) {
                if (totalChecked++ > 1000) break;
                try {
                    BaseBlock block = clipboard.getBlock(vec);
                    if (block != null && block.getId() != BlockID.AIR) {
                        nonAirBlocks++;
                    }
                } catch (Exception e) {
                }
            }
            Bukkit.broadcastMessage("[FAWE] Clipboard verification: " + nonAirBlocks + "/" + Math.min(totalChecked, 1000) + " sampled blocks are non-air");

            return clipboard;
        } catch (Exception e) {
            Bukkit.broadcastMessage("[FAWE] Error creating clipboard:");
            e.printStackTrace();
            return null;
        }
    }

    public boolean validateClipboard(Clipboard clipboard) {
        if (clipboard == null) {
            Bukkit.broadcastMessage("[FAWE] Clipboard validation failed: clipboard is null");
            return false;
        }

        if (clipboard.getRegion() == null) {
            Bukkit.broadcastMessage("[FAWE] Clipboard validation failed: region is null");
            return false;
        }

        long blockCount = clipboard.getRegion().getArea();
        Bukkit.broadcastMessage("[FAWE] Clipboard validation: " + blockCount + " blocks");

        if (blockCount > 1000000) { // 1 million blocks
            Bukkit.broadcastMessage("[FAWE] Warning: Very large clipboard (" + blockCount + " blocks)");
        }

        return true;
    }


    public void pasteClipboard(Clipboard clipboard, Location targetLocation, Runnable callback) {
        if (!validateClipboard(clipboard)) {
            if (callback != null) callback.run();
            return;
        }

        if (targetLocation.getWorld() == null) {
            Bukkit.broadcastMessage("[FAWE] Target world is null for pasting");
            if (callback != null) callback.run();
            return;
        }

        World world = getWEWorld(targetLocation.getWorld());
        if (world == null) {
            Bukkit.broadcastMessage("[FAWE] Failed to get WorldEdit world for pasting");
            if (callback != null) callback.run();
            return;
        }

        Vector targetVec = new Vector(
                targetLocation.getBlockX(),
                targetLocation.getBlockY(),
                targetLocation.getBlockZ()
        );
        long blockCount = clipboard.getRegion().getArea();

        Bukkit.broadcastMessage("[FAWE] Pasting " + blockCount + " blocks to " + targetVec);
        Bukkit.broadcastMessage("[FAWE] Clipboard region: " + clipboard.getRegion().getMinimumPoint() + " to " + clipboard.getRegion().getMaximumPoint());


        try {
            long startTime = System.currentTimeMillis();

            // Create the paste operation
            ForwardExtentCopy paste = new ForwardExtentCopy(clipboard, clipboard.getRegion(), world, targetVec);
            paste.setRemovingEntities(true);

            // Execute synchronously
            Operations.complete(paste);

            long endTime = System.currentTimeMillis();
            Bukkit.broadcastMessage("[FAWE] Paste completed successfully in " + (endTime - startTime) + "ms");

            return;
        } catch (Exception e) {
            Bukkit.broadcastMessage("[FAWE] Error during paste operation:");
            e.printStackTrace();
            return;
        }
    }

    private World getWEWorld(org.bukkit.World bukkitWorld) {
        try {
            return new BukkitWorld(bukkitWorld);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}