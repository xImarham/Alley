package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.impl.killeffect.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffect.KillEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.killeffect.impl.NoneKillEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.impl.NoneSoundEffect;
import dev.revere.alley.feature.cosmetic.interfaces.ICosmetic;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
@Setter
public class ProfileCosmeticData {
    protected final Alley plugin = Alley.getInstance();
    private String selectedKillEffect;
    private String selectedSoundEffect;

    public ProfileCosmeticData() {
        KillEffectRepository killEffectRepository = this.plugin.getCosmeticRepository().getCosmeticRepository(KillEffectRepository.class);
        this.selectedKillEffect = killEffectRepository.getCosmetics().isEmpty() ? "None" : killEffectRepository.getCosmetic(NoneKillEffect.class).getName();

        SoundEffectRepository soundEffectRepository = this.plugin.getCosmeticRepository().getCosmeticRepository(SoundEffectRepository.class);
        this.selectedSoundEffect = soundEffectRepository.getCosmetics().isEmpty() ? "None" : soundEffectRepository.getCosmetic(NoneSoundEffect.class).getName();
    }

    /**
     * Set the active cosmetic
     *
     * @param type the type of cosmetic
     * @param name the cosmetic to set
     */
    public void setActiveCosmetic(String type, ICosmetic name) {
        if (type.equalsIgnoreCase("KillEffect")) {
            this.selectedKillEffect = name.getName();
        } else if (type.equalsIgnoreCase("SoundEffect")) {
            this.selectedSoundEffect = name.getName();
        }
    }

    /**
     * Get the active cosmetic
     *
     * @param type the type of cosmetic
     * @return the active cosmetic
     */
    public String getActiveCosmetic(String type) {
        if (type.equalsIgnoreCase("KillEffect")) {
            return this.selectedKillEffect != null ? this.selectedKillEffect : "None";
        } else if (type.equalsIgnoreCase("SoundEffect")) {
            return this.selectedSoundEffect != null ? this.selectedSoundEffect : "None";
        }
        return "None";
    }

    /**
     * Get the active cosmetic
     *
     * @param cosmetic the cosmetic to get
     * @return the active cosmetic
     */
    public String getActiveCosmetic(ICosmetic cosmetic) {
        if (cosmetic instanceof AbstractKillEffect) {
            return this.selectedKillEffect != null ? this.selectedKillEffect : "None";
        } else if (cosmetic instanceof AbstractSoundEffect) {
            return this.selectedSoundEffect != null ? this.selectedSoundEffect : "None";
        }
        return "None";
    }

    /**
     * Check if the cosmetic is selected
     *
     * @param cosmetic the cosmetic to check
     * @return true if the cosmetic is selected
     */
    public boolean isSelectedCosmetic(ICosmetic cosmetic) {
        if (cosmetic instanceof AbstractKillEffect) {
            return this.selectedKillEffect != null && this.selectedKillEffect.equals(cosmetic.getName());
        } else if (cosmetic instanceof AbstractSoundEffect) {
            return this.selectedSoundEffect != null && this.selectedSoundEffect.equals(cosmetic.getName());
        }
        return false;
    }
}