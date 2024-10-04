package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import org.bukkit.potion.PotionEffectType;

@UtilityClass
public class PotionUtil {
    /**
     * Get the name of a potion effect type.
     *
     * @param potionEffectType The potion effect type.
     * @return The name of the potion effect type.
     */
    public String getName(PotionEffectType potionEffectType) {
        switch (potionEffectType.getName()) {
            case "fire_resistance": {
                return "Fire Resistance";
            }
            case "speed": {
                return "Fire Speed";
            }
            case "weakness": {
                return "Weakness";
            }
            case "slowness": {
                return "Slowness";
            }
            default: {
                return "Unknown";
            }
        }
    }
}