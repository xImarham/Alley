package dev.revere.alley.profile.division.impl.master;

import dev.revere.alley.profile.division.AbstractDivision;
import dev.revere.alley.profile.division.annotation.DivisionData;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@DivisionData(
        name = "Master 1",
        description = "The first division of the master tier",
        icon = Material.INK_SACK,
        tier = EnumDivisionTier.MASTER,
        level = EnumDivisionLevel.LEVEL_1,
        durability = 12,
        slot = 29)
public class MasterDivision1 extends AbstractDivision {

}
