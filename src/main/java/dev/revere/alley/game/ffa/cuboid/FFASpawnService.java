package dev.revere.alley.game.ffa.cuboid;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.tool.cuboid.Cuboid;
import dev.revere.alley.tool.cuboid.CuboidService;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.alley.tool.serializer.Serializer;
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

        this.spawn = Serializer.deserializeLocation(config.getString("arenas." + arena.getName() + ".pos1"));
        this.minimum = Serializer.deserializeLocation(config.getString("arenas." + arena.getName() + ".safe-zone.pos1"));
        this.maximum = Serializer.deserializeLocation(config.getString("arenas." + arena.getName() + ".safe-zone.pos2"));

        if (this.minimum == null || this.maximum == null) {
            Logger.logError("FFA safezone not found! Please set the ffa arena safezone and save it using the /arena save command.");
            return;
        }

        this.cuboid = new Cuboid(this.minimum, this.maximum);
    }

    public Cuboid getCuboid() {
        if (this.cuboid == null) {
            Logger.logError("Cuboid is not initialized. Please load the cuboid first.");
            return null;
        }
        return cuboid;
    }

    @Override
    public void updateCuboid() {

    }
}