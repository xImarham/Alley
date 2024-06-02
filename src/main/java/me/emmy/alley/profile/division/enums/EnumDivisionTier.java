package me.emmy.alley.profile.division.enums;

import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public enum EnumDivisionTier {
    BRONZE("Bronze", 1100, 0),
    SILVER("Silver", 1200, 1100),
    GOLD("Gold", 1300, 1200),
    PLATINUM("Platinum", 1400, 1300),
    DIAMOND("Diamond", 1500, 1400),
    MASTER("Master", 1600, 1500),
    GRANDMASTER("Grandmaster", 1700, 1600),

    ;

    private final String name;
    private final int maxElo;
    private final int minElo;

    private static final int LEVELS_PER_TIER = 3;
    private static final int ELO_INCREMENT_PER_LEVEL = 33;

    /**
     * Constructor for the EnumDivisionTier
     *
     * @param name   the name of the tier
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
        return new int[]{minElo, Math.min(maxElo, this.maxElo)};
    }

    /**
     * Get the next division and level
     *
     * @param level the level
     * @return the next division and level
     */
    public String getNextDivisionAndLevel(EnumDivisionLevel level) {
        EnumDivisionLevel[] levels = EnumDivisionLevel.values();
        int levelIndex = level.ordinal();

        if (levelIndex < levels.length - 1) {
            return this.name + " " + levels[levelIndex + 1].getName();
        } else {
            EnumDivisionTier[] tiers = EnumDivisionTier.values();
            int tierIndex = this.ordinal();
            if (tierIndex == tiers.length - 1) {
                EnumDivisionTier nextTier = tiers[tierIndex + 1];
                return nextTier.getName() + " " + levels[0].getName();
            } else {
                return this.name + " " + levels[levelIndex].getName();
            }
        }
    }

    /**
     * Get the next division and level array
     *
     * @param level the level
     * @return the next division and level array
     */
    public String[] getNextDivisionAndLevelArray(EnumDivisionLevel level) {
        EnumDivisionLevel[] levels = EnumDivisionLevel.values();
        int currentLevelIndex = level.ordinal();

        if (currentLevelIndex < levels.length - 1) {
            return new String[]{this.name, levels[currentLevelIndex + 1].getName().replace("Level ", "")};
        } else {
            EnumDivisionTier[] tiers = EnumDivisionTier.values();
            int currentTierIndex = this.ordinal();
            if (currentTierIndex < tiers.length - 1) {
                EnumDivisionTier nextTier = tiers[currentTierIndex + 1];
                return new String[]{nextTier.getName(), levels[0].getName().replace("Level ", "")};
            } else {
                return new String[]{this.name, levels[currentLevelIndex].getName().replace("Level ", "")};
            }
        }
    }

    /**
     * Get the next division
     *
     * @return the next division
     */
    public EnumDivisionTier getNextDivision() {
        EnumDivisionTier[] tiers = EnumDivisionTier.values();
        int tierIndex = this.ordinal();

        if (tierIndex < tiers.length - 1) {
            return tiers[tierIndex + 1];
        } else {
            return this;
        }
    }

    /**
     * Get the next level
     *
     * @param level the level
     * @return the next level
     */
    public EnumDivisionLevel getNextLevel(EnumDivisionLevel level) {
        EnumDivisionLevel[] levels = EnumDivisionLevel.values();
        int levelIndex = level.ordinal();

        if (levelIndex < levels.length - 1) {
            return levels[levelIndex + 1];
        } else {
            return levels[0];
        }
    }

    /**
     * Get the min elo for a specific level
     *
     * @param level the level
     * @return the min elo
     */
    public int getMinEloForLevel(EnumDivisionLevel level) {
        return this.minElo + (level.ordinal() * ELO_INCREMENT_PER_LEVEL);
    }
}
