package dev.revere.alley.cosmetic.repository;

import dev.revere.alley.cosmetic.interfaces.ICosmetic;
import dev.revere.alley.util.logger.Logger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public abstract class BaseCosmeticRepository<T extends ICosmetic> {
    private final List<T> cosmetics = new ArrayList<>();

    /**
     * Register a cosmetic class to the repository
     *
     * @param clazz The class to register
     */
    protected void registerCosmetic(Class<? extends T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            this.cosmetics.add(instance);
        } catch (Exception e) {
            Logger.logError("Failed to register cosmetic class " + clazz.getSimpleName() + ": " + e.getMessage());
        }
    }

    /**
     * Get a cosmetic by its name
     *
     * @param name The name of the cosmetic
     * @return The cosmetic
     */
    public T getByName(String name) {
        for (T cosmetic : this.cosmetics) {
            if (cosmetic.getName().equals(name)) {
                return cosmetic;
            }
        }
        return null;
    }
}