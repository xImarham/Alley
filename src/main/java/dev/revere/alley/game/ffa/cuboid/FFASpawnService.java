package dev.revere.alley.game.ffa.cuboid;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.util.cuboid.Cuboid;
import dev.revere.alley.util.cuboid.CuboidService;
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
public class FFASpawnService extends CuboidService {
    private Location minimum;
    private Location maximum;
    private Location spawn;
    private Cuboid cuboid;

    public FFASpawnService() {
        this.loadCuboid();
    }

    /**
     * Load the FFA spawn location from the arenas.yml file
     */
    public void loadCuboid() {
        FileConfiguration config = Alley.getInstance().getConfigService().getArenasConfig();
        AbstractArena arena = Alley.getInstance().getArenaService().getArenas().stream()
                .filter(a -> a.getType() == EnumArenaType.FFA)
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

        this.cuboid = new Cuboid(this.minimum, this.maximum);
    }

    @Override
    public void updateCuboid() {

    }
}