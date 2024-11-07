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
        name = "Diamond 1",
        description = "The first division of the diamond tier",
        icon = Material.DIAMOND,
        tier = EnumDivisionTier.DIAMOND,
        level = EnumDivisionLevel.LEVEL_1,
        slot = 24)
public class DiamondDivision1 extends AbstractDivision {
}
