package me.emmy.alley.arena;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.arena.impl.FreeForAllArena;
import me.emmy.alley.arena.impl.SharedArena;
import me.emmy.alley.arena.impl.StandAloneArena;
import me.emmy.alley.utils.others.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 20/05/2024 - 16:54
 */

@Getter
public class ArenaManager {

    private final List<Arena> arenas = new ArrayList<>();

    public void loadArenas() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/arenas.yml");

        ConfigurationSection arenasConfig = config.getConfigurationSection("arenas");
        if (arenasConfig == null) {
            return;
        }

        for (String name : arenasConfig.getKeys(false)) {
            String arenaName = "arenas." + name;

            ArenaType arenaType = ArenaType.valueOf(config.getString(arenaName + ".type"));

            Location minimum = LocationUtil.deserialize(config.getString(arenaName + ".minimum"));
            Location maximum = LocationUtil.deserialize(config.getString(arenaName + ".maximum"));

            Arena arena;

            switch(arenaType) {
                case SHARED:
                    arena = new SharedArena(
                            name,
                            minimum,
                            maximum
                    );
                    break;
                case STANDALONE:
                    arena = new StandAloneArena(
                            name,
                            minimum,
                            maximum
                    );
                    break;
                case FFA:
                    arena = new FreeForAllArena(
                            name,
                            minimum,
                            maximum
                    );
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + arenaType);
            }

            if (config.contains(arenaName + ".kits")) {
                for (String kitName : config.getStringList(config + ".kits")) {
                    arena.getKits().add(Alley.getInstance().getKitManager().getKit(kitName));
                }
            }

            if (config.contains(arenaName + ".pos1")) {
                arena.setPos1();
            }

        }

    }
}
