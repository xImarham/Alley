package me.emmy.alley.profile.cosmetic.impl.soundeffect.impl;

import me.emmy.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.annotation.SoundEffectData;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@SoundEffectData(name = "Step", description = "Play step sound upon kill", permission = "step", icon = Material.REDSTONE, slot = 11)
public class StepSoundEffect extends AbstractSoundEffect {

    @Override
    public void spawnEffect(Player player) {
        player.playEffect(player.getLocation(), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }
}
