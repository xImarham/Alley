package dev.revere.alley.profile.division.impl.silver;

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
        name = "Silver 2",
        description = "The second division of the silver tier",
        icon = Material.IRON_INGOT,
        tier = EnumDivisionTier.SILVER,
        level = EnumDivisionLevel.LEVEL_2,
        slot = 14)
public class SilverDivision2 extends AbstractDivision {
}
