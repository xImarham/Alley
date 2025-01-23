package dev.revere.alley.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import dev.revere.alley.arena.ArenaType;
import dev.revere.alley.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
public class SharedArena extends Arena {
    /**
     * Constructor for the SharedArena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public SharedArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public ArenaType getType() {
        return ArenaType.SHARED;
    }

    @Override
    public void createArena() {
        Alley.getInstance().getArenaRepository().getArenas().add(this);
        saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + getName();
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");
        config.set(name, null);
        config.set(name + ".type", getType().name());
        config.set(name + ".minimum", LocationUtil.serialize(getMinimum()));
        config.set(name + ".maximum", LocationUtil.serialize(getMaximum()));
        config.set(name + ".center", LocationUtil.serialize(getCenter()));
        config.set(name + ".pos1", LocationUtil.serialize(getPos1()));
        config.set(name + ".pos2", LocationUtil.serialize(getPos2()));
        config.set(name + ".kits", getKits());
        config.set(name + ".enabled", isEnabled());
        config.set(name + ".displayName", getDisplayName());
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");
        config.set("arenas." + getName(), null);

        Alley.getInstance().getArenaRepository().getArenas().remove(this);
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }
}