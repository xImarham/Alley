package me.emmy.alley.killeffects;

import lombok.Data;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */

@Data
public abstract class AbstractKillEffect {
    private final String name;
    private final String description;
    private final Material icon;
    public abstract void spawnEffect(Location location);
}
