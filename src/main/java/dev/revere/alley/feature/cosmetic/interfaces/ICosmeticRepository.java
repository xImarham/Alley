package dev.revere.alley.feature.cosmetic.interfaces;

import dev.revere.alley.feature.cosmetic.EnumCosmeticType;

import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
public interface ICosmeticRepository<T extends ICosmetic> {
    List<T> getCosmetics();
    T getCosmetic(String name);
    /**
     * Add this method to the interface. It declares the category of the repository.
     * @return The CosmeticType that this repository manages.
     */
    EnumCosmeticType getRepositoryType();
}