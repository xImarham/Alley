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
        name = "Bronze 1",
        description = "The first division of the bronze tier",
        icon = Material.CLAY_BRICK,
        tier = EnumDivisionTier.BRONZE,
        level = EnumDivisionLevel.LEVEL_1,
        slot = 10)
public class BronzeDivision1 extends AbstractDivision {
}
