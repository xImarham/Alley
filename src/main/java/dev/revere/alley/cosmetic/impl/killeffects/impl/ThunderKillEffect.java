package dev.revere.alley.cosmetic.impl.killeffects.impl;

import dev.revere.alley.cosmetic.impl.killeffects.AbstractKillEffect;
import dev.revere.alley.cosmetic.impl.killeffects.annotation.KillEffectData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@KillEffectData(name = "Thunder", description = "Spawn a lighting bolt at the opponent", permission = "thunder", icon = Material.STICK, slot = 11)
public class ThunderKillEffect extends AbstractKillEffect {

    @Override
    public void spawnEffect(Player player) {
        Location location = player.getLocation();
        location.getWorld().strikeLightningEffect(location);
    }
}
