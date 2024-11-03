package me.emmy.alley.essential.spawn;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.util.logger.Logger;
import me.emmy.alley.util.location.LocationUtil;
import me.emmy.alley.util.chat.CC;
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
public class SpawnService {
    private Location location;

    public SpawnService() {
        loadSpawnLocation();
    }

    /**
     * Load the spawn location from the settings.yml file
     */
    private void loadSpawnLocation() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("settings.yml");

        Location location = LocationUtil.deserialize(config.getString("spawn.join-location"));

        if (location == null) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4&l(!) SPAWN LOCATION IS NULL (!)"));
            return;
        }

        this.location = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Update or set the spawn location and save it in the settings.yml file
     *
     * @param location the location to set
     */
    public void updateSpawnLocation(Location location) {
        this.location = location;
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("settings.yml");

        config.set("spawn.join-location", LocationUtil.serialize(location));

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFile("settings.yml"), config);
    }

    /**
     * Teleport the player to the spawn location
     *
     * @param player the player to teleport
     */
    public void teleportToSpawn(Player player) {
        Location spawnLocation = getLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        } else {
            Logger.logError("Spawn location is null.");
        }
    }
}