package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.annotation.KillEffectData;
import dev.revere.alley.util.particle.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@KillEffectData(name = "Explosion", description = "Spawn explosion particles", permission = "explosion", icon = Material.TNT, slot = 13)
public class ExplosionKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        ParticleEffect.EXPLOSION_LARGE.display(0.5f, 0.5f, 0.5f, 1.0f, 12, player.getLocation(), 20.0);
    }
}