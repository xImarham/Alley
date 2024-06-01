package me.emmy.alley.profile.cosmetic.impl.killeffects;

import lombok.Getter;
import me.emmy.alley.profile.cosmetic.impl.killeffects.impl.BloodKillEffect;
import me.emmy.alley.profile.cosmetic.impl.killeffects.impl.NoneKillEffect;
import me.emmy.alley.profile.cosmetic.impl.killeffects.impl.ThunderKillEffect;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmeticRepository;
import me.emmy.alley.profile.cosmetic.repository.BaseCosmeticRepository;

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