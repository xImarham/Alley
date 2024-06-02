package me.emmy.alley.profile.division.impl.platinum;

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
        name = "Platinum 2",
        description = "The second division of the platinum tier",
        icon = Material.INK_SACK,
        tier = EnumDivisionTier.PLATINUM,
        level = EnumDivisionLevel.LEVEL_2,
        durability = 4,
        slot = 22)
public class PlatinumDivision2 extends AbstractDivision {
}
