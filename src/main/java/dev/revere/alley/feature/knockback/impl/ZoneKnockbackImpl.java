package dev.revere.alley.feature.knockback.impl;

import dev.revere.alley.feature.knockback.IKnockback;
import dev.revere.alley.feature.knockback.enums.EnumKnockbackType;
import dev.revere.alley.tool.logger.Logger;
import dev.revere.spigot.knockback.KnockbackAPI;
import org.bukkit.entity.Player;

import java.util.Set;

/**
 * @author Remi
 * @project alley-practice
 * @since 29/06/2025
 */
public class ZoneKnockbackImpl implements IKnockback {
    @Override
    public EnumKnockbackType getType() {
        return EnumKnockbackType.ZONE;
    }

    @Override
    public void applyKnockback(Player player, String profile) {
        if (profile == null || profile.trim().isEmpty()) {
            KnockbackAPI.setPlayerProfile(player.getUniqueId(), KnockbackAPI.getActiveProfileName());
            return;
        }

        Set<String> profiles = KnockbackAPI.getAvailableProfileNames();

        boolean profileExists = profiles.stream().anyMatch(p -> p.equalsIgnoreCase(profile));

        if (!profileExists) {
            Logger.error("Attempted to apply a knockback profile that does not exist");
            return;
        }

        boolean success = KnockbackAPI.setPlayerProfile(player.getUniqueId(), profile);
        if (!success) {
            Logger.error("Failed to apply the " + profile + " knockback profile for " + player.getName());
        }
    }
}