package me.emmy.alley.spawn;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.location.LocationUtil;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 17/05/2024 - 17:47
 */
@Getter
public class SpawnHandler {

    private Location joinLocation;

    /**
     * Load the spawn location from the settings.yml file
     */
    public void loadSpawnLocation() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("settings.yml");

        Location location = LocationUtil.deserialize(config.getString("spawn.join-location"));

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4&l(!) SPAWN LOCATION IS NULL (!)"));
            return;
        }

        joinLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Set the spawn location in the settings.yml file
     *
     * @param location the location to set
     */
    public void setSpawnLocation(Location location) {
        this.joinLocation = location;
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("settings.yml");

        config.set("spawn.join-location", LocationUtil.serialize(location));

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("settings.yml"), config);
    }

    /**
     * Teleport the player to the spawn location
     *
     * @param player the player to teleport
     */
    public void teleportToSpawn(Player player) {
        Location spawnLocation = getJoinLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4&l(!) SPAWN LOCATION IS NULL (!)"));
        }
    }
}
