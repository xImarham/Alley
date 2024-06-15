package me.emmy.alley.ffa.safezone;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.ffa.safezone.cuboid.Cuboid;
import me.emmy.alley.utils.chat.CC;
import me.emmy.alley.utils.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 12/06/2024 - 22:14
 */
@Getter
@Setter
public class FFASpawnHandler {

    private final Alley plugin = Alley.getInstance();

    private Location spawnLocation;
    private Location safezoneMin;
    private Location safezoneMax;

    private Cuboid cuboid;

    public void loadFFASpawn() {
        FileConfiguration config = ConfigHandler.getInstance().getArenasConfig();
        Arena arena = Alley.getInstance().getArenaRepository().getArenas().stream()
                .filter(a -> a.getType() == ArenaType.FFA)
                .findFirst()
                .orElse(null);

        if (arena != null) {
            this.spawnLocation = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".pos1"));
            this.safezoneMin = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safezone.pos1"));
            this.safezoneMax = LocationUtil.deserialize(config.getString("arenas." + arena.getName() + ".safezone.pos2"));
        } else {
            CC.broadcast("&4 [!] FFA arena not found!");
            return;
        }

        if (this.safezoneMin != null && this.safezoneMax != null) {
            CC.broadcast("&a [!] FFA safezoneMin: " + this.safezoneMin.toString());
            CC.broadcast("&a [!] FFA safezoneMax: " + this.safezoneMax.toString());
            this.cuboid = new Cuboid(safezoneMin, safezoneMax);
        } else {
            CC.broadcast("&4 [!] FFA safezone not found!");
        }
    }
}
