package me.emmy.alley.profile.cosmetic.repository;

import lombok.Getter;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.SoundEffectRepository;
import me.emmy.alley.profile.cosmetic.impl.killeffects.KillEffectRepository;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmeticRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
public class CosmeticRepository {
    private final Map<String, ICosmeticRepository<?>> cosmeticRepositories = new HashMap<>();

    public CosmeticRepository() {
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
        cosmeticRepositories.put(name, repository);
    }
}