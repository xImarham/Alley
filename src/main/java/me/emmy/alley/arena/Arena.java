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
    private List<Kit> kits = new ArrayList<>();
    private boolean enabled;

    public Arena(String name, String displayName, Location pos1, Location pos2, Location center, Location minimum, Location maximum) {
        this.name = name;
        this.displayName = displayName;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.center = center;
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Arena(String name, Location minimum, Location maximum) {
        this.name = name;
        this.minimum = minimum;
        this.maximum = maximum;
    }
}
