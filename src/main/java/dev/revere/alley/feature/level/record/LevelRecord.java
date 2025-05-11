package dev.revere.alley.feature.level.record;

import lombok.Getter;
import org.bukkit.Material;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
public class LevelRecord {
    private final String name;
    private final String displayName;

    private final Material material;
    private final int durability;

    private final int minElo;
    private final int maxElo;

    /**
     * Constructor for the LevelRecord class.
     *
     * @param name        The name of the level.
     * @param displayName The display name of the level.
     * @param material    The material associated with this level.
     * @param durability  The durability of the material.
     * @param minElo      The minimum Elo rating for this level.
     * @param maxElo      The maximum Elo rating for this level.
     */
    public LevelRecord(String name, String displayName, Material material, int durability, int minElo, int maxElo) {
        this.name = name;
        this.displayName = displayName;
        this.material = material;
        this.durability = durability;
        this.minElo = minElo;
        this.maxElo = maxElo;
    }
}