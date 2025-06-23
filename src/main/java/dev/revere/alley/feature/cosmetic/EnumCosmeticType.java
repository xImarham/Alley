package dev.revere.alley.feature.cosmetic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@Getter
@RequiredArgsConstructor
public enum EnumCosmeticType {
    KILL_EFFECT("killeffect", "Display a fancy effect upon killing a player."),
    SOUND_EFFECT("soundeffect", "Play a custom sound when you get a kill."),
    PROJECTILE_TRAIL("projectiletrail", "Leave a nice particle trail on your projectiles.");

    private final String permissionKey;
    private final String description;

    /**
     * Finds a CosmeticType from a user-friendly string, ignoring case, dashes, and underscores.
     *
     * @param input The string provided by the user (e.g., "KillEffect", "kill-effect").
     * @return The matching EnumCosmeticType, or null if not found.
     */
    public static EnumCosmeticType fromString(String input) {
        if (input == null) {
            return null;
        }
        String sanitizedInput = input.replace("-", "").replace("_", "").toUpperCase();

        for (EnumCosmeticType type : values()) {
            String sanitizedEnumName = type.name().replace("_", "");
            if (sanitizedEnumName.equals(sanitizedInput)) {
                return type;
            }

            String sanitizedPermissionKey = type.permissionKey.replace("-", "").replace("_", "").toUpperCase();
            if (sanitizedPermissionKey.equals(sanitizedInput)) {
                return type;
            }
        }
        return null;
    }
}
