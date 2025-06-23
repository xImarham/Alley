package dev.revere.alley.feature.cosmetic.impl.projectiletrail.impl;

import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.feature.cosmetic.impl.projectiletrail.AbstractProjectileTrail;
import dev.revere.alley.util.particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@CosmeticData(
        type = EnumCosmeticType.PROJECTILE_TRAIL,
        name = "Flame Trail",
        description = "Leave a blazing trail behind your projectiles!",
        icon = Material.BLAZE_POWDER,
        slot = 10,
        price = 1000
)
public class FlameTrail extends AbstractProjectileTrail {
    @Override
    public void spawnTrailParticle(Location location) {
        World world = location.getWorld();
        if (world == null) return;
        ParticleEffect.FLAME.display(0F, 0F, 0F, 0F, 1, location, 64.0);
        ParticleEffect.SMOKE_LARGE.display(0F, 0F, 0F, 0F, 1, location, 64.0);
    }
}