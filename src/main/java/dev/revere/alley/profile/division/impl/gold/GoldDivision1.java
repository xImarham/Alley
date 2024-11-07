package dev.revere.alley.profile.division.impl.gold;

import dev.revere.alley.profile.division.AbstractDivision;
import dev.revere.alley.profile.division.annotation.DivisionData;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@DivisionData(
        name = "Gold 1",
        description = "The first division of the gold tier",
        icon = Material.GOLD_INGOT,
        tier = EnumDivisionTier.GOLD,
        level = EnumDivisionLevel.LEVEL_1,
        slot = 16)
public class GoldDivision1 extends AbstractDivision {
}
