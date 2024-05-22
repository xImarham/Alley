package me.emmy.alley.spawn;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 17/05/2024 - 17:47
 */

@Getter
public class SpawnManager {

    private Location spawnLocation;

    public void loadSpawnLocation() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("settings.yml");

        World world = Bukkit.getWorld(config.getString("spawn-location.world"));
        double x = config.getDouble("spawn-location.x");
        double y = config.getDouble("spawn-location.y");
        double z = config.getDouble("spawn-location.z");
        float yaw = (float) config.getDouble("spawn-location.yaw");
        float pitch = (float) config.getDouble("spawn-location.pitch");

        spawnLocation = new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("settings.yml");

        config.set("spawn-location.world", location.getWorld().getName());
        config.set("spawn-location.x", location.getX());
        config.set("spawn-location.y", location.getY());
        config.set("spawn-location.z", location.getZ());
        config.set("spawn-location.yaw", location.getYaw());
        config.set("spawn-location.pitch", location.getPitch());

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("settings.yml"), config);
    }
}
