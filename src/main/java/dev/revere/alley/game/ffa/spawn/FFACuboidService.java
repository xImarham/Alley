package dev.revere.alley.game.ffa.spawn;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.arena.ArenaType;
import dev.revere.alley.util.cuboid.Cuboid;
import dev.revere.alley.util.location.LocationUtil;
import dev.revere.alley.util.logger.Logger;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 12/06/2024 - 22:14
 */
@Getter
public class FFACuboidService {
    private Location minimum;
    private Location maximum;
    private Location spawn;
    private Cuboid cuboid;

    public FFACuboidService() {
        this.loadFFASpawn();
    }

    /**
     * Load the FFA spawn location from the arenas.yml file
     */
    public void loadFFASpawn() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getArenasConfig();
        Arena arena = Alley.getInstance().getArenaRepository().getArenas().stream()
                .filter(a -> a.getType() == ArenaType.FFA)
                .findFirst()
                .orElse(null);

        if (arena == null) {
            Logger.logError("FFA arena not found!");
            return;
        }

        this.spawn = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".pos1"));
        this.minimum = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safezone.pos1"));
        this.maximum = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safezone.pos2"));

        if (this.minimum == null || this.maximum == null) {
            Logger.logError("FFA safezone not found! Please set the ffa arena safezone and save it using the /arena save command.");
            return;
        }

        this.cuboid = new Cuboid(minimum, maximum);
    }
}