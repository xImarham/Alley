package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.feature.cosmetic.interfaces.ICosmetic;
import dev.revere.alley.tool.logger.Logger;
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
    private final List<T> cosmetics;

    public BaseCosmeticRepository() {
        this.cosmetics = new ArrayList<>();
    }

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
     * Method to retrieve a cosmetic by its name.
     *
     * @param name The name of the cosmetic
     * @return The cosmetic
     */
    public T getCosmetic(String name) {
        for (T cosmetic : this.cosmetics) {
            if (cosmetic.getName().equals(name)) {
                return cosmetic;
            }
        }
        return null;
    }

    /**
     * Method to retrieve a cosmetic by its class.
     *
     * @param clazz The class of the cosmetic
     * @return The cosmetic
     */
    public T getCosmetic(Class<? extends T> clazz) {
        for (T cosmetic : this.cosmetics) {
            if (cosmetic.getClass().equals(clazz)) {
                return cosmetic;
            }
        }
        return null;
    }
}