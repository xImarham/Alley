package me.emmy.alley.killeffects.impl;

import me.emmy.alley.killeffects.AbstractKillEffect;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */
public class NoneKillEffect extends AbstractKillEffect {
    public NoneKillEffect() {
        super("Remove Effect", "Remove your effect", Material.REDSTONE);
    }

    @Override
    public void spawnEffect(Location location) {}
}
