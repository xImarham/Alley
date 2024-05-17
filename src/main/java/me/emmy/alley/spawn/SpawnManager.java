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

        World world = Bukkit.getWorld(config.getString("on-join.teleport.location.world"));
        double x = config.getDouble("on-join.teleport.location.x");
        double y = config.getDouble("on-join.teleport.location.y");
        double z = config.getDouble("on-join.teleport.location.z");
        float yaw = (float) config.getDouble("on-join.teleport.location.yaw");
        float pitch = (float) config.getDouble("on-join.teleport.location.pitch");

        spawnLocation = new Location(world, x, y, z, yaw, pitch);
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("settings.yml");

        config.set("on-join.teleport.location.world", location.getWorld().getName());
        config.set("on-join.teleport.location.x", location.getX());
        config.set("on-join.teleport.location.y", location.getY());
        config.set("on-join.teleport.location.z", location.getZ());
        config.set("on-join.teleport.location.yaw", location.getYaw());
        config.set("on-join.teleport.location.pitch", location.getPitch());

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("settings.yml"), config);
    }
}
