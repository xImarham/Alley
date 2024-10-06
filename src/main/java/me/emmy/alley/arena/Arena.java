package me.emmy.alley.arena;

import me.emmy.alley.config.ConfigHandler;

import me.emmy.alley.util.chat.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 16:42
 */
@Getter
@Setter
public abstract class Arena {
    private String name;
    private String displayName;
    private Location pos1;
    private Location pos2;
    private Location center;
    private Location minimum;
    private Location maximum;
    private ArenaType type;
    private List<String> kits = new ArrayList<>();
    private boolean enabled;

    /**
     * Constructor for the Arena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public Arena(String name, Location minimum, Location maximum) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public abstract void saveArena();
    public abstract void createArena();
    public abstract void deleteArena();
}
