package me.emmy.alley.profile.division.impl.silver;

import me.emmy.alley.profile.division.AbstractDivision;
import me.emmy.alley.profile.division.annotation.DivisionData;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@DivisionData(
        name = "Silver 1",
        description = "The first division of the silver tier",
        icon = Material.IRON_INGOT,
        tier = EnumDivisionTier.SILVER,
        level = EnumDivisionLevel.LEVEL_1,
        slot = 13)
public class SilverDivision1 extends AbstractDivision {
}
