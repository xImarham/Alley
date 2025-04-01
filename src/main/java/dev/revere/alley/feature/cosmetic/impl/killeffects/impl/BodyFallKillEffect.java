package dev.revere.alley.feature.cosmetic.impl.killeffects.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffects.annotation.KillEffectData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@KillEffectData(name = "Body Fall", description = "Let's the opponent's body fall.", permission = "alley.cosmetic.killeffect.bodyfall", icon = Material.DIAMOND_SWORD, slot = 16)
public class BodyFallKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        Alley.getInstance().getDeathReflectionService().animateDeath(player);
    }
}