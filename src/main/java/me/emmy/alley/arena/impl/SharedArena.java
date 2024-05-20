package me.emmy.alley.arena.impl;

import me.emmy.alley.arena.Arena;
import org.bukkit.Location;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 20/05/2024 - 19:14
 */

public class SharedArena extends Arena {

    public SharedArena(String name, Location minimum, Location maximum) {
        super(name, minimum, maximum);
    }
}
