package dev.revere.alley.profile.division.impl.bronze;

import dev.revere.alley.profile.division.AbstractDivision;
import dev.revere.alley.profile.division.annotation.DivisionData;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@DivisionData(
        name = "Bronze 2",
        description = "The second division of the bronze tier",
        icon = Material.CLAY_BRICK,
        tier = EnumDivisionTier.BRONZE,
        level = EnumDivisionLevel.LEVEL_2,
        slot = 11)
public class BronzeDivision2 extends AbstractDivision {
}
