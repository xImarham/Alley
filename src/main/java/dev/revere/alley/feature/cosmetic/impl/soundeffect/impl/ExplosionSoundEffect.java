package dev.revere.alley.feature.cosmetic.impl.soundeffect.impl;

import dev.revere.alley.feature.cosmetic.impl.soundeffect.AbstractSoundEffect;
import dev.revere.alley.feature.cosmetic.impl.soundeffect.annotation.SoundEffectData;
import dev.revere.alley.util.SoundUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@SoundEffectData(name = "Explosion", description = "Play an explosion sound", permission = "alley.cosmetic.soundeffect.explosion", icon = Material.TNT, slot = 12)
public class ExplosionSoundEffect extends AbstractSoundEffect {

    @Override
    public void spawnEffect(Player player) {
        SoundUtil.playCustomSound(player, Sound.EXPLODE, 1.0f, 1.0f);
    }
}
