package dev.revere.alley.feature.cosmetic.impl.killeffect.impl;

import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.annotation.CosmeticData;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@CosmeticData(type = EnumCosmeticType.KILL_EFFECT, name = "None", description = "Remove your kill effect", icon = Material.BARRIER, slot = 10)
public class NoneKillEffect extends AbstractCosmetic {
    @Override
    public void execute(Player player) {

    }
}
