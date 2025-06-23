package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import dev.revere.alley.tool.reflection.impl.DeathReflectionService;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@CosmeticData(type = EnumCosmeticType.KILL_EFFECT, name = "Body Fall", description = "Let's the opponent's body fall.", permission = "bodyfall", icon = Material.DIAMOND_SWORD, slot = 16)
public class BodyFallKillEffect extends AbstractCosmetic {

    @Override
    public void execute(Player player) {
        Alley.getInstance().getReflectionRepository().getReflectionService(DeathReflectionService.class).animateDeath(player);
    }
}