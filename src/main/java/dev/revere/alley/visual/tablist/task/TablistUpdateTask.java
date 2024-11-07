package dev.revere.alley.visual.tablist.task;

import dev.revere.alley.visual.tablist.ITablist;
import dev.revere.alley.visual.tablist.impl.TablistVisualizer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @date 07/09/2024 - 15:23
 */
public class TablistUpdateTask extends BukkitRunnable {

    private final ITablist tablistVisualizer;

    public TablistUpdateTask() {
        this.tablistVisualizer = new TablistVisualizer();
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            tablistVisualizer.update(player);
        }
    }
}