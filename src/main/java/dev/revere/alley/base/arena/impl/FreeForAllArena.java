package dev.revere.alley.base.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.tool.serializer.Serializer;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:15
 */
public class FreeForAllArena extends AbstractArena {
    protected final Alley plugin = Alley.getInstance();

    /**
     * Constructor for the FreeForAllArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public FreeForAllArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.FFA;
    }

    @Override
    public void createArena() {
        this.plugin.getArenaService().getArenas().add(this);
        this.saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + this.getName();
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();

        config.set(name, null);
        config.set(name + ".type", this.getType().name());
        config.set(name + ".safe-zone.pos1", Serializer.serializeLocation(this.getMinimum()));
        config.set(name + ".safe-zone.pos2", Serializer.serializeLocation(this.getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(this.getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(this.getPos1()));
        config.set(name + ".enabled", this.isEnabled());
        config.set(name + ".display-name", this.getDisplayName());

        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();
        config.set("arenas." + this.getName(), null);

        this.plugin.getArenaService().getArenas().remove(this);
        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/arenas.yml"), config);
    }
}