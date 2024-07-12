package me.emmy.alley.utils.location;

import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Alley
 * @date 10/06/2024 - 20:50
 */
@UtilityClass
public class RayTracerUtil {
    public Location rayTrace(Location startLocation, Vector direction) {
        Location currentLocation = startLocation.clone();
        while (currentLocation.getBlock().getType() == Material.AIR) {
            currentLocation.add(direction);
        }
        return currentLocation;
    }
}
