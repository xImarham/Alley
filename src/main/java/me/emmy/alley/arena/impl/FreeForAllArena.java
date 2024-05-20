package me.emmy.alley.arena.impl;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.arena.ArenaType;
import me.emmy.alley.utils.others.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 20/05/2024 - 19:15
 */

public class FreeForAllArena extends Arena {

    /**
     * Constructor for the FreeForAllArena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public FreeForAllArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public ArenaType getType() {
        return ArenaType.FFA;
    }

    @Override
    public void createArena() {
        Alley.getInstance().getArenaRepository().getArenas().add(this);
        saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + getName();

        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/arenas.yml");
        config.set(name, null);
        config.set(name + ".type", getType().name());
        config.set(name + ".minimum", LocationUtil.serialize(getMinimum()));
        config.set(name + ".maximum", LocationUtil.serialize(getMaximum()));
        config.set(name + ".pos1", LocationUtil.serialize(getPos1()));
        config.set(name + ".pos2", LocationUtil.serialize(getPos2()));
        config.set(name + ".kits", getKits());
        config.set(name + ".enabled", isEnabled());
        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        super.deleteArena();

        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/arenas.yml");
        config.set("arenas." + getName(), null);

        Alley.getInstance().getArenaRepository().getArenas().remove(this);
        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/arenas.yml"), config);
    }
}
