package me.emmy.alley.arena;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.kit.Kit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 20/05/2024 - 16:42
 */

@Getter
@Setter
public class Arena {
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

    public void createArena() {}
    public void saveArena() {}
    public void deleteArena() {}
}
