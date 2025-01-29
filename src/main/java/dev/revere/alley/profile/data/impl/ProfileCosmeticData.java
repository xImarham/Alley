package dev.revere.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.cosmetic.impl.soundeffect.SoundEffectRepository;
import dev.revere.alley.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.cosmetic.impl.killeffects.KillEffectRepository;
import dev.revere.alley.cosmetic.interfaces.ICosmetic;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
@Setter
public class ProfileCosmeticData {
    private String selectedKillEffect;
    private String selectedSoundEffect;

    public ProfileCosmeticData() {
        KillEffectRepository killEffectRepository = new KillEffectRepository();
        this.selectedKillEffect = killEffectRepository.getCosmetics().isEmpty() ? "None" : killEffectRepository.getCosmetics().get(0).getName();

        SoundEffectRepository soundEffectRepository = new SoundEffectRepository();
        this.selectedSoundEffect = soundEffectRepository.getCosmetics().isEmpty() ? "None" : soundEffectRepository.getCosmetics().get(0).getName();
    }

    /**
     * Set the active cosmetic
     *
     * @param type the type of cosmetic
     * @param name the cosmetic to set
     */
    public void setActiveCosmetic(String type, ICosmetic name) {
        if (type.equalsIgnoreCase("KillEffect")) {
            selectedKillEffect = name.getName();
        } else if (type.equalsIgnoreCase("SoundEffect")) {
            selectedSoundEffect = name.getName();
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
            return selectedKillEffect != null ? selectedKillEffect : "None";
        } else if (type.equalsIgnoreCase("SoundEffect")) {
            return selectedSoundEffect != null ? selectedSoundEffect : "None";
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
            return selectedKillEffect != null ? selectedKillEffect : "None";
        } else if (cosmetic instanceof AbstractSoundEffect) {
            return selectedSoundEffect != null ? selectedSoundEffect : "None";
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
            return selectedKillEffect != null && selectedKillEffect.equals(cosmetic.getName());
        } else if (cosmetic instanceof AbstractSoundEffect) {
            return selectedSoundEffect != null && selectedSoundEffect.equals(cosmetic.getName());
        }
        return false;
    }
}