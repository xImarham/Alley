package dev.revere.alley.task;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 19:24
 */
public class ArrowRemovalTask extends BukkitRunnable {
    @Override
    public void run() {
        Bukkit.getLogger().info("[ArrowRemovalTask] Running arrow removal check...");

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity instanceof Arrow) {
                    Arrow arrow = (Arrow) entity;
                    Bukkit.getLogger().info("[ArrowRemovalTask] Found arrow: " + arrow.getUniqueId());

                    if (arrow.isOnGround()) {
                        Bukkit.getLogger().info("[ArrowRemovalTask] Removing arrow: " + arrow.getUniqueId());
                        arrow.remove();
                    }
                }
            }
        }
    }
}