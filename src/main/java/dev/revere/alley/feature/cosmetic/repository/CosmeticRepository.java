package dev.revere.alley.feature.cosmetic.repository;

import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.killmessage.KillMessageRepository;
import dev.revere.alley.feature.cosmetic.impl.projectiletrail.ProjectileTrailRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
@Service(provides = ICosmeticRepository.class, priority = 140)
public class CosmeticRepository implements ICosmeticRepository {
    private final Map<EnumCosmeticType, BaseCosmeticRepository<?>> repositories = new EnumMap<>(EnumCosmeticType.class);

    @Override
    public void initialize(AlleyContext context) {
        this.register(new KillEffectRepository());
        this.register(new SoundEffectRepository());
        this.register(new ProjectileTrailRepository());
        this.register(new KillMessageRepository());

        Logger.info("Registered " + this.repositories.size() + " cosmetic repositories.");
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

    @Override
    public BaseCosmeticRepository<?> getRepository(EnumCosmeticType type) {
        return this.repositories.get(type);
    }

    @Override
    public <T extends BaseCosmeticRepository<?>> T getRepository(EnumCosmeticType type, Class<T> repositoryClass) {
        BaseCosmeticRepository<?> repo = getRepository(type);
        if (repositoryClass.isInstance(repo)) {
            return repositoryClass.cast(repo);
        }
        return null;
    }
}