package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.killmessage.KillMessageRepository;
import dev.revere.alley.feature.cosmetic.impl.projectiletrail.ProjectileTrailRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public class CosmeticRepository {
    private final Map<EnumCosmeticType, BaseCosmeticRepository<?>> repositories;

    public CosmeticRepository() {
        this.repositories = new EnumMap<>(EnumCosmeticType.class);

        this.register(new KillEffectRepository());
        this.register(new SoundEffectRepository());
        this.register(new ProjectileTrailRepository());
        this.register(new KillMessageRepository());
    }

    /**
     * Registers a repository, using its declared CosmeticType as the key.
     *
     * @param repository The repository instance to register.
     */
    private void register(BaseCosmeticRepository<?> repository) {
        EnumCosmeticType type = repository.getRepositoryType();
        if (type != null) {
            this.repositories.put(type, repository);
        }
    }

    /**
     * Gets a cosmetic repository by its CosmeticType.
     *
     * @param type The CosmeticType of the repository to retrieve.
     * @return The repository instance, or null if not found.
     */
    public BaseCosmeticRepository<?> getRepository(EnumCosmeticType type) {
        return this.repositories.get(type);
    }
}