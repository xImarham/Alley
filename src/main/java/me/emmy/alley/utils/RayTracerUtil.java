package me.emmy.alley.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 10/06/2024 - 20:50
 */
public class RayTracerUtil {
    public static Location rayTrace(Location startLocation, Vector direction) {
        Location currentLocation = startLocation.clone();
        while (currentLocation.getBlock().getType() == Material.AIR) {
            currentLocation.add(direction);
        }
        return currentLocation;
    }
}
