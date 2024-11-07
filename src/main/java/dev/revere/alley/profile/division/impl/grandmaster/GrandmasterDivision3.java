package dev.revere.alley.profile.division.impl.grandmaster;

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
        name = "Grandmaster 3",
        description = "The third division of the grandmaster tier",
        icon = Material.INK_SACK,
        tier = EnumDivisionTier.GRANDMASTER,
        level = EnumDivisionLevel.LEVEL_3,
        durability = 6,
        slot = 34)
public class GrandmasterDivision3 extends AbstractDivision {

}
