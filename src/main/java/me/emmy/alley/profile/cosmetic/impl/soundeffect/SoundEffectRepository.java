package me.emmy.alley.profile.cosmetic.impl.soundeffect;

import lombok.Getter;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.impl.StepSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.impl.NoneSoundEffect;
import me.emmy.alley.profile.cosmetic.interfaces.BaseCosmeticRepository;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmeticRepository;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class SoundEffectRepository extends BaseCosmeticRepository<AbstractSoundEffect> implements ICosmeticRepository<AbstractSoundEffect> {
    /**
     * Constructor to register all kill effects
     */
    public SoundEffectRepository() {
        registerCosmetic(NoneSoundEffect.class);
        registerCosmetic(StepSoundEffect.class);
    }
}
