package me.emmy.alley.profile.cosmetic.killeffects;

import lombok.Data;
import me.emmy.alley.profile.cosmetic.killeffects.annotation.KillEffectData;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author Zion
 * @project Alley
 * @date 01/06/2024
 */
@Data
public abstract class AbstractKillEffect {
    private final String name = getClass().getAnnotation(KillEffectData.class).name();
    private final String description = getClass().getAnnotation(KillEffectData.class).description();
    private final Material icon = getClass().getAnnotation(KillEffectData.class).icon();

    /**
     * Spawn the effect at the given location
     *
     * @param location the location to spawn the effect
     */
    public abstract void spawnEffect(Location location);
}
