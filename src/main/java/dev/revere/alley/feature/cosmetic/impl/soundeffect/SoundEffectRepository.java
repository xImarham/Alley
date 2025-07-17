package dev.revere.alley.feature.cosmetic.impl.soundeffect;

import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.ExplosionSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.NoneSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.StepSoundEffect;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmeticRepository;
import dev.revere.alley.feature.cosmetic.repository.BaseCosmeticRepository;
import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Getter
public class SoundEffectRepository extends BaseCosmeticRepository<AbstractCosmetic> {
    public SoundEffectRepository() {
        this.registerCosmetic(ExplosionSoundEffect.class);
        this.registerCosmetic(NoneSoundEffect.class);
        this.registerCosmetic(StepSoundEffect.class);
    }
}