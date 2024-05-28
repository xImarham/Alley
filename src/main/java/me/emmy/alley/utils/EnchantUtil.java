package me.emmy.alley.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 28/05/2024 - 20:53
 */

public class EnchantUtil {

    private static final Map<String, String> ENCHANTMENTS = new HashMap<>();

    static {
        ENCHANTMENTS.put("sharpness", "DAMAGE_ALL");
        ENCHANTMENTS.put("efficiency", "DIG_SPEED");
        ENCHANTMENTS.put("unbreaking", "DURABILITY");
        ENCHANTMENTS.put("fortune", "LOOT_BONUS_BLOCKS");
        ENCHANTMENTS.put("power", "ARROW_DAMAGE");
        ENCHANTMENTS.put("punch", "ARROW_KNOCKBACK");
        ENCHANTMENTS.put("flame", "ARROW_FIRE");
        ENCHANTMENTS.put("infinity", "ARROW_INFINITE");
    }

    public static String getEnchantment(String key) {
        return ENCHANTMENTS.get(key.toLowerCase());
    }
}

