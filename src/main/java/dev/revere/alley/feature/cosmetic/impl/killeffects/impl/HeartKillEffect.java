package dev.revere.alley.feature.cosmetic.impl.killeffects.impl;

import dev.revere.alley.feature.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffects.annotation.KillEffectData;
import dev.revere.alley.util.particle.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@KillEffectData(name = "Heart", description = "Spawn hearts at the opponent", permission = "alley.cosmetic.killeffect.heart", icon = Material.REDSTONE, slot = 15)
public class HeartKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        ParticleEffect.HEART.display(0.4f, 0.4f, 0.4f, 0.1f, 10, player.getLocation(), 20.0);
    }
}