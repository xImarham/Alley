package dev.revere.alley.cosmetic.impl.killeffects.impl;

import dev.revere.alley.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.cosmetic.impl.killeffects.annotation.KillEffectData;
import dev.revere.alley.util.particle.ParticleEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@KillEffectData(name = "Blood", description = "Spawn blood particles", permission = "blood", icon = Material.REDSTONE, slot = 12)
public class BloodKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        Location location = player.getLocation();
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
        ParticleEffect.ITEM_CRACK.display(new ParticleEffect.ItemData(Material.REDSTONE, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
        ParticleEffect.BLOCK_DUST.display(new ParticleEffect.BlockData(Material.REDSTONE_BLOCK, (byte) 0), 0.5f, 0.5f, 0.5f, 0.1f, 50, location, 20.0);
    }
}
