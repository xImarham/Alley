package dev.revere.alley.profile.division;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.profile.division.annotation.DivisionData;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
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
    private final int durability;
    private final int slot;
    private int eloMin;
    private int eloMax;

    public AbstractDivision() {
        DivisionData data = getClass().getAnnotation(DivisionData.class);
        if (data != null) {
            this.name = data.name();
            this.description = data.description();
            this.durability = data.durability();
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

    public String[] getNextDivisionAndLevelArray() {
        return this.tier.getNextDivisionAndLevelArray(this.level);
    }

    public String getNextDivisionAndLevel() {
        String nextDivisionAndLevel = this.tier.getNextDivisionAndLevel(this.level);
        return nextDivisionAndLevel.replace("Level ", "");
    }

    public EnumDivisionTier getNextDivision() {
        return this.tier.getNextDivision();
    }

    public EnumDivisionLevel getNextLevel() {
        return this.tier.getNextLevel(this.level);
    }

    /**
     * Get the elo needed to reach the target division and level
     *
     * @param targetTier  the target tier
     * @param targetLevel the target level
     * @return the elo needed
     */
    public int getEloNeededForDivision(EnumDivisionTier targetTier, EnumDivisionLevel targetLevel) {
        int targetElo = targetTier.getMinEloForLevel(targetLevel);
        return targetElo - this.eloMin;
    }

    /**
     * Get the progress bar for the division
     *
     * @param eloRequired the elo required
     * @return the progress bar
     */
    public String generateProgressBar(int eloRequired) {
        int progressBarLength = 20;
        int progress = (int) Math.ceil((double) (this.eloMax - eloRequired - this.eloMin) / (this.eloMax - this.eloMin) * progressBarLength);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < progressBarLength; i++) {
            if (i < progress) {
                builder.append("§a§l┃");
            } else {
                builder.append("§7§l┃");
            }
        }
        return builder.toString();
    }
}
