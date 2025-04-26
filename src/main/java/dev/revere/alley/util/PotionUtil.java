package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * Serialize a list of potion effects.
     *
     * @param potionEffects The potion effects.
     * @return The serialized potion effects.
     */
    public List<String> serialize(List<PotionEffect> potionEffects) {
        return potionEffects.stream()
                   .map(effect -> effect.getType().getName() + ":" + effect.getDuration() + ":" + effect.getAmplifier())
                   .collect(Collectors.toList());
    }

    /**
     * Deserialize a list of potion effects.
     *
     * @param serializedEffects The serialized potion effects.
     * @return The potion effects.
     */
    public List<PotionEffect> deserialize(List<String> serializedEffects) {
        return serializedEffects.stream()
                   .map(s -> {
                       String[] parts = s.split(":");
                       if (parts.length < 3) return null;
                       PotionEffectType type = PotionEffectType.getByName(parts[0]);
                       int duration = Integer.parseInt(parts[1]);
                       int amplifier = Integer.parseInt(parts[2]);
                       return type != null ? new PotionEffect(type, duration, amplifier) : null;
                   })
                   .filter(Objects::nonNull)
                   .collect(Collectors.toList());
    }
}