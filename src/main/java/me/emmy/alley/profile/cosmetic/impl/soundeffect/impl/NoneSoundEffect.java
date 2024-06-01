package me.emmy.alley.profile.cosmetic.impl.soundeffect.impl;

import me.emmy.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.annotation.SoundEffectData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@SoundEffectData(name = "None", description = "Remove your sound effect", icon = Material.BARRIER, slot = 10)
public class NoneSoundEffect extends AbstractSoundEffect {

    @Override
    public void spawnEffect(Player player) {

    }
}
