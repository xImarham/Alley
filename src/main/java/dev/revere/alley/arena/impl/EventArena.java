package dev.revere.alley.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.arena.Arena;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 12/10/2024 - 22:49
 */
public class EventArena extends Arena {
    /**
     * Constructor for the Arena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public EventArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }

    @Override
    public void saveArena() {
        String name = "arenas." + getName();

        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/arenas.yml");
        config.set(name, null);
        config.set(name + ".type", getType().name());

    }

    @Override
    public void createArena() {

    }

    @Override
    public void deleteArena() {

    }
}