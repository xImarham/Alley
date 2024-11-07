package dev.revere.alley.profile.division.impl.gold;

import dev.revere.alley.profile.division.AbstractDivision;
import dev.revere.alley.profile.division.annotation.DivisionData;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@DivisionData(
        name = "Gold 2",
        description = "The second division of the gold tier",
        icon = Material.GOLD_INGOT,
        tier = EnumDivisionTier.GOLD,
        level = EnumDivisionLevel.LEVEL_2,
        slot = 19)
public class GoldDivision2 extends AbstractDivision {
}
