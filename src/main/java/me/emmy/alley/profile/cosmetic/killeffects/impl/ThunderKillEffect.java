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
@KillEffectData(name = "Thunder Effect", description = "Spawn a lighting bolt at the opponent", icon = Material.REDSTONE)
public class ThunderKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Location location) {
        location.getWorld().strikeLightningEffect(location);
    }
}
