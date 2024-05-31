package me.emmy.alley.profile.cosmetic.killeffects.impl;

import me.emmy.alley.profile.cosmetic.killeffects.AbstractKillEffect;
import me.emmy.alley.profile.cosmetic.killeffects.annotation.KillEffectData;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */
@KillEffectData(name = "Remove Effect", description = "Remove your effect", icon = Material.REDSTONE)
public class NoneKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Location location) {

    }
}
