package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.util.particle.ParticleEffect;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = EnumCosmeticType.KILL_EFFECT, name = "Heart", description = "Spawn hearts at the opponent", permission = "heart", icon = Material.REDSTONE, slot = 15)
public class HeartKillEffect extends AbstractCosmetic {

    @Override
    public void execute(Player player) {
        ParticleEffect.HEART.display(0.4f, 0.4f, 0.4f, 0.1f, 10, player.getLocation(), 20.0);
    }
}