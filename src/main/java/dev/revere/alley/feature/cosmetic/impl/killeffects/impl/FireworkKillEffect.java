package dev.revere.alley.feature.cosmetic.impl.killeffects.impl;

import dev.revere.alley.feature.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.feature.cosmetic.impl.killeffects.annotation.KillEffectData;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.stream.IntStream;

/**
 * @author Emmy
 * @project Alley
 * @since 02/04/2025
 */
@KillEffectData(name = "Firework", description = "Spawn a firework at the opponent", permission = "alley.cosmetic.killeffect.firework", icon = Material.FIREWORK, slot = 14)
public class FireworkKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        IntStream.range(0, 3).forEach(i -> player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK));
    }
}