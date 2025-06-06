package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public class CosmeticRepository {
    private final Map<String, ICosmeticRepository<?>> cosmeticRepositories;

    public CosmeticRepository() {
        this.cosmeticRepositories = new HashMap<>();
        this.registerCosmeticRepository("KillEffect", new KillEffectRepository());
        this.registerCosmeticRepository("SoundEffect", new SoundEffectRepository());
    }

    /**
     * Register a cosmetic repository
     *
     * @param name       the name of the repository
     * @param repository the repository
     */
    private void registerCosmeticRepository(String name, ICosmeticRepository<?> repository) {
        this.cosmeticRepositories.put(name, repository);
    }

    /**
     * Get a cosmetic repository by its class type.
     *
     * @param clazz the class type of the cosmetic repository to retrieve.
     * @param <T>   the type of the cosmetic repository, extending ICosmeticRepository.
     * @return the cosmetic repository instance if found, or null if not found.
     */
    public <T extends ICosmeticRepository<?>> T getCosmeticRepository(Class<T> clazz) {
        for (ICosmeticRepository<?> repository : this.cosmeticRepositories.values()) {
            if (clazz.isInstance(repository)) {
                return clazz.cast(repository);
            }
        }
        return null; // or throw an exception if not found
    }
}