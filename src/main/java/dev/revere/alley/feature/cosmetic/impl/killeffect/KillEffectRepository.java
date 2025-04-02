package dev.revere.alley.feature.cosmetic.impl.killeffect;

import dev.revere.alley.feature.cosmetic.impl.killeffect.impl.*;
import lombok.Getter;
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
        this.registerCosmetic(BloodKillEffect.class);
        this.registerCosmetic(BodyFallKillEffect.class);
        this.registerCosmetic(ExplosionKillEffect.class);
        this.registerCosmetic(FireworkKillEffect.class);
        this.registerCosmetic(HeartKillEffect.class);
        this.registerCosmetic(NoneKillEffect.class);
        this.registerCosmetic(ThunderKillEffect.class);
    }
}