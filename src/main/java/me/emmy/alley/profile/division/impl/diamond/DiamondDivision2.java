package me.emmy.alley.profile.division.impl.diamond;

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
        name = "Diamond 2",
        description = "The second division of the diamond tier",
        icon = Material.DIAMOND,
        tier = EnumDivisionTier.DIAMOND,
        level = EnumDivisionLevel.LEVEL_2,
        slot = 25)
public class DiamondDivision2 extends AbstractDivision {
}
