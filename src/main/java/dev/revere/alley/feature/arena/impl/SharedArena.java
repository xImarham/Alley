package dev.revere.alley.feature.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.AbstractArena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.tool.serializer.Serializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
public class SharedArena extends AbstractArena {
    /**
     * Constructor for the SharedArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public SharedArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.SHARED;
    }

    @Override
    public void createArena() {
        Alley.getInstance().getArenaService().getArenas().add(this);
        saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + getName();
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");

        config.set(name, null);
        config.set(name + ".type", getType().name());
        config.set(name + ".minimum", Serializer.serializeLocation(getMinimum()));
        config.set(name + ".maximum", Serializer.serializeLocation(getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(getPos1()));
        config.set(name + ".pos2", Serializer.serializeLocation(getPos2()));
        config.set(name + ".kits", getKits());
        config.set(name + ".enabled", isEnabled());
        config.set(name + ".displayName", getDisplayName());

        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");
        config.set("arenas." + getName(), null);

        Alley.getInstance().getArenaService().getArenas().remove(this);
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }
}