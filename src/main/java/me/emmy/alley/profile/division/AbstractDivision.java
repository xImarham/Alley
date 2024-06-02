package me.emmy.alley.profile.division;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.profile.division.annotation.DivisionData;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import org.bukkit.Material;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Setter
@Getter
public class AbstractDivision {

    private final String name;
    private final String description;
    private final Material icon;
    private final EnumDivisionTier tier;
    private final EnumDivisionLevel level;
    private final int slot;
    private int eloMin;
    private int eloMax;

    public AbstractDivision() {
        DivisionData data = getClass().getAnnotation(DivisionData.class);
        if (data != null) {
            this.name = data.name();
            this.description = data.description();
            this.icon = data.icon();
            this.tier = data.tier();
            this.level = data.level();
            this.slot = data.slot();

            int[] eloRange = tier.getEloRangeForLevel(level);
            this.eloMin = eloRange[0];
            this.eloMax = eloRange[1];
        } else {
            throw new IllegalStateException("DivisionData annotation missing");
        }
    }
}
