package me.emmy.alley.profile.division.impl.gold;

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
        name = "Gold 1",
        description = "The first division of the gold tier",
        icon = Material.GOLD_INGOT,
        tier = EnumDivisionTier.GOLD,
        level = EnumDivisionLevel.LEVEL_1,
        slot = 16)
public class GoldDivision1 extends AbstractDivision {
}
