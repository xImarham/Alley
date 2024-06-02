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
        name = "Grandmaster 1",
        description = "The first division of the grandmaster tier",
        icon = Material.INK_SACK,
        tier = EnumDivisionTier.GRANDMASTER,
        level = EnumDivisionLevel.LEVEL_1,
        durability = 6,
        slot = 32)
public class GrandmasterDivision1 extends AbstractDivision {

}
