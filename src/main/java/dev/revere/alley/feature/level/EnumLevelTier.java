package dev.revere.alley.feature.level;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
public enum EnumLevelTier {
    AMATEUR("Amateur", 0, 1050),
    FIGHTER("Fighter", 1051, 1150),
    EXPERIENCED("Experienced", 1151, 1250),
    GLADIATOR("Gladiator", 1251, 1350),
    CHAMPION("Champion", 1351, 1450),
    LEGEND("Legend", 1451, 1550),
    ELITE("Elite", 1551, 1650),
    IMMORTAL("Immortal", 1651, 1750),
    THREAT("Threat", 1751, Integer.MAX_VALUE);

    private final String displayName;
    private final int minElo;
    private final int maxElo;

    /**
     * Constructor for the EnumLevelTier enum.
     *
     * @param displayName The display name of the tier.
     * @param minElo      The minimum Elo rating for the tier.
     * @param maxElo      The maximum Elo rating for the tier.
     */
    EnumLevelTier(String displayName, int minElo, int maxElo) {
        this.displayName = displayName;
        this.minElo = minElo;
        this.maxElo = maxElo;
    }

    public static EnumLevelTier fromElo(int elo) {
        for (EnumLevelTier tier : values()) {
            if (elo >= tier.minElo && elo <= tier.maxElo) return tier;
        }
        return AMATEUR;
    }
}