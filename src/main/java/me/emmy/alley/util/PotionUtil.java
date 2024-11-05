package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Emmy
 * @project Alley
 * @date 03/11/2024 - 20:56
 */
@UtilityClass
public class PotionUtil {
    /**
     * Get the potion effect type of an item stack.
     *
     * @param item The item stack.
     * @return The potion effect type.
     */
    public PotionEffectType getPotionEffectType(ItemStack item) {
        Potion potion = Potion.fromItemStack(item);
        if (!potion.getEffects().isEmpty()) {
            return potion.getEffects().iterator().next().getType();
        }
        return null;
    }

    /**
     * Get the potion effect amplifier of an item stack.
     *
     * @param item The item stack.
     * @return The potion effect amplifier.
     */
    public int getPotionEffectAmplifier(ItemStack item) {
        Potion potion = Potion.fromItemStack(item);
        if (!potion.getEffects().isEmpty()) {
            return potion.getEffects().iterator().next().getAmplifier();
        }
        return 0;
    }

    /**
     * Get the name of a potion effect type.
     *
     * @param potionEffectType The potion effect type.
     * @return The name of the potion effect type.
     */
    public String getName(PotionEffectType potionEffectType) {
        switch (potionEffectType.getName()) {
            case "fire_resistance":
                return "Fire Resistance";
            case "speed":
                return "Fire Speed";
            case "weakness":
                return "Weakness";
            case "slowness":
                return "Slowness";
            default:
                return "Unknown";
        }
    }

    /**
     * Serialize a potion effect and convert it into a string.
     *
     * @param potionEffect The potion effect.
     * @return The serialized potion effect.
     */
    public String serialize(PotionEffect potionEffect) {
        return potionEffect.getType().getName() + ":" + potionEffect.getDuration() + ":" + potionEffect.getAmplifier();
    }

    /**
     * Deserialize a potion effect and convert it into a new potion effect.
     *
     * @param string The serialized potion effect.
     * @return The potion effect.
     */
    public PotionEffect deserialize(String string) {
        String[] parts = string.split(":");
        return new PotionEffect(PotionEffectType.getByName(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
    }
}