package dev.revere.alley.feature.knockback.impl;

import dev.revere.alley.feature.knockback.IKnockback;
import dev.revere.alley.feature.knockback.enums.EnumKnockbackType;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @since 29/06/2025
 */
public class DefaultKnockbackImpl implements IKnockback {
    @Override
    public EnumKnockbackType getType() {
        return EnumKnockbackType.DEFAULT;
    }

    @Override
    public void applyKnockback(Player player, String profile) {

    }
}