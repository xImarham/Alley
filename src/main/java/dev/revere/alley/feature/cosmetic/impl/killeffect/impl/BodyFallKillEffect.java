package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.annotation.KillEffectData;
import dev.revere.alley.tool.reflection.impl.DeathReflectionService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@KillEffectData(name = "Body Fall", description = "Let's the opponent's body fall.", permission = "bodyfall", icon = Material.DIAMOND_SWORD, slot = 16)
public class BodyFallKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        Alley.getInstance().getReflectionRepository().getReflectionService(DeathReflectionService.class).animateDeath(player);
    }
}