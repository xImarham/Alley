package dev.revere.alley.util;

import dev.revere.alley.feature.cosmetic.EnumCosmeticType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    /**
     * A helper method to convert an enum constant name into a user-friendly, title-cased string.
     * Example: KILL_EFFECT -> Kill Effect
     *
     * @param type The EnumCosmeticType to format.
     * @return A formatted string.
     */
    public String formatCosmeticTypeName(EnumCosmeticType type) {
        String lowerCase = type.name().replace('_', ' ').toLowerCase();

        String[] words = lowerCase.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return result.toString().trim();
    }
}
