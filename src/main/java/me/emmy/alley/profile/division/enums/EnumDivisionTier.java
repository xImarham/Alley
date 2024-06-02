package me.emmy.alley.profile.division.enums;

import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public enum EnumDivisionTier {
    BRONZE("Bronze", 1200, 0),
    SILVER("Silver", 1400, 1200),
    GOLD("Gold", 1600, 1400),
    PLATINUM("Platinum", 1800, 1600),
    DIAMOND("Diamond", 2000, 1800),
    MASTER("Master", 2200, 2000),
    GRANDMASTER("Grandmaster", 2400, 2200),

    ;

    private final String name;
    private final int maxElo;
    private final int minElo;

    private static final int LEVELS_PER_TIER = 3;
    private static final int ELO_INCREMENT_PER_LEVEL = 50;

    /**
     * Constructor for the EnumDivisionTier
     *
     * @param name the name of the tier
     * @param maxElo the max elo of the tier
     * @param minElo the min elo of the tier
     */
    EnumDivisionTier(String name, int maxElo, int minElo) {
        this.name = name;
        this.maxElo = maxElo;
        this.minElo = minElo;
    }

    /**
     * Get the elo range for a specific level
     *
     * @param level the level
     * @return the elo range
     */
    public int[] getEloRangeForLevel(EnumDivisionLevel level) {
        int levelIndex = level.ordinal();
        int minElo = this.minElo + (levelIndex * ELO_INCREMENT_PER_LEVEL);
        int maxElo = minElo + ELO_INCREMENT_PER_LEVEL - 1;
        return new int[] {minElo, Math.min(maxElo, this.maxElo)};
    }
}
