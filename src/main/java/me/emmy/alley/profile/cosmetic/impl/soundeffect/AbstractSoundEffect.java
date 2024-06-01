package me.emmy.alley.profile.cosmetic.impl.soundeffect;

import lombok.Data;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.annotation.SoundEffectData;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmetic;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project Alley
 * @date 01/06/2024
 */
@Data
public abstract class AbstractSoundEffect implements ICosmetic {
    private final String name;
    private final String description;
    private final String permission;
    private final Material icon;
    private final int slot;

    public AbstractSoundEffect() {
        SoundEffectData data = getClass().getAnnotation(SoundEffectData.class);
        if (data != null) {
            this.name = data.name();
            this.description = data.description();
            this.permission = data.permission();
            this.icon = data.icon();
            this.slot = data.slot();
        } else {
            throw new IllegalStateException("SoundEffectData annotation missing");
        }
    }

    /**
     * Spawns the effect at the given player location
     *
     * @param player the player to spawn the effect for
     */
    public abstract void spawnEffect(Player player);

    /**
     * Gets the permission for this kill effect
     *
     * @return the permission for this kill effect
     */
    public String getPermission() {
        return "alley.sound.effects." + this.permission;
    }
}
