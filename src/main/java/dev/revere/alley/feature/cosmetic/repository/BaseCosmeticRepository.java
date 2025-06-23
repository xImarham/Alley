package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public abstract class BaseCosmeticRepository<T extends AbstractCosmetic> implements ICosmeticRepository<T> {
    private final Map<String, T> cosmeticsByName;

    public BaseCosmeticRepository() {
        this.cosmeticsByName = new HashMap<>();
    }

    /**
     * Register a cosmetic class to the repository
     *
     * @param clazz The class to register
     */
    protected void registerCosmetic(Class<? extends T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            this.cosmeticsByName.put(instance.getName(), instance);
        } catch (Exception e) {
            Logger.logError("Failed to register cosmetic class " + clazz.getSimpleName() + ": " + e.getMessage());
        }
    }

    @Override
    public EnumCosmeticType getRepositoryType() {
        if (cosmeticsByName.isEmpty()) {
            return null;
        }
        return cosmeticsByName.values().iterator().next().getType();
    }

    @Override
    public List<T> getCosmetics() {
        return new ArrayList<>(this.cosmeticsByName.values());
    }

    @Override
    public T getCosmetic(String name) {
        return this.cosmeticsByName.get(name);
    }
}