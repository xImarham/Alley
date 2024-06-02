package me.emmy.alley.profile.division.impl.grandmaster;

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
        name = "Grandmaster 2",
        description = "The second division of the grandmaster tier",
        icon = Material.INK_SACK,
        tier = EnumDivisionTier.GRANDMASTER,
        level = EnumDivisionLevel.LEVEL_2,
        durability = 6,
        slot = 33)
public class GrandmasterDivision2 extends AbstractDivision {

}
