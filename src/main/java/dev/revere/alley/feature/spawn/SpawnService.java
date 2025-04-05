package dev.revere.alley.feature.spawn;

import dev.revere.alley.config.ConfigService;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.util.location.LocationUtil;
import lombok.Getter;
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
    private final ConfigService configService;
    private Location location;

    /**
     * Constructor for the SpawnService class.
     *
     * @param configService The config service.
     */
    public SpawnService(ConfigService configService) {
        this.configService = configService;
        this.loadSpawnLocation();
    }

    private void loadSpawnLocation() {
        FileConfiguration config = this.configService.getSettingsConfig();
        Location location = LocationUtil.deserialize(config.getString("spawn.join-location"));
        if (location == null) {
            Logger.logError("Spawn location is null.");
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
        FileConfiguration config = this.configService.getSettingsConfig();
        config.set("spawn.join-location", LocationUtil.serialize(location));

        this.location = location;
        this.configService.saveConfig(this.configService.getConfigFile("settings.yml"), config);
    }

    /**
     * Teleport the player to the spawn location
     *
     * @param player the player to teleport
     */
    public void teleportToSpawn(Player player) {
        Location spawnLocation = this.location;
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        } else {
            Logger.logError("Spawn location is null.");
        }
    }
}