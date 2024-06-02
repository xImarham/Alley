package me.emmy.alley.profile.division.impl.bronze;

import me.emmy.alley.profile.division.AbstractDivision;
import me.emmy.alley.profile.division.annotation.DivisionData;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@DivisionData(
        name = "Bronze 3",
        description = "The third division of the bronze tier",
        icon = Material.IRON_INGOT,
        tier = EnumDivisionTier.BRONZE,
        level = EnumDivisionLevel.LEVEL_3,
        slot = 12)
public class BronzeDivision3 extends AbstractDivision {
}
