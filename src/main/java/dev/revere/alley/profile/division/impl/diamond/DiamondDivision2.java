package dev.revere.alley.profile.division.impl.diamond;

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
        name = "Diamond 2",
        description = "The second division of the diamond tier",
        icon = Material.DIAMOND,
        tier = EnumDivisionTier.DIAMOND,
        level = EnumDivisionLevel.LEVEL_2,
        slot = 25)
public class DiamondDivision2 extends AbstractDivision {
}
