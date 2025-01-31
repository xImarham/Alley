package dev.revere.alley.feature.cosmetic.impl.killeffects;

import lombok.Getter;
import dev.revere.alley.feature.cosmetic.impl.killeffects.impl.BloodKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffects.impl.NoneKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffects.impl.ThunderKillEffect;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class KillEffectRepository extends BaseCosmeticRepository<AbstractKillEffect> implements ICosmeticRepository<AbstractKillEffect> {
    /**
     * Constructor to register all kill effects
     */
    public KillEffectRepository() {
        registerCosmetic(NoneKillEffect.class);
        registerCosmetic(ThunderKillEffect.class);
        registerCosmetic(BloodKillEffect.class);
    }
}