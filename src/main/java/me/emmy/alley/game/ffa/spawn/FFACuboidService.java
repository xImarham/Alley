package me.emmy.alley.game.ffa.spawn;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.util.cuboid.Cuboid;
import me.emmy.alley.util.chat.Logger;
import me.emmy.alley.util.location.LocationUtil;
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
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();
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

        Logger.debug("FFA spawn location: " + this.spawn);
        Logger.debug("FFA safezoneMin: " + this.minimum);
        Logger.debug("FFA safezoneMax: " + this.maximum);

        this.cuboid = new Cuboid(minimum, maximum);
    }
}