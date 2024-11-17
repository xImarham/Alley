package dev.revere.alley.cooldown.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @date 27/10/2024 - 08:43
 */
@Getter
public enum EnumCooldownType {
    ENDER_PEARL(15 * 1000L),
    PARTY_ANNOUNCE_COOLDOWN(600 * 1000L),

    ;

    private final long cooldownDuration;

    /**
     * Constructor for the EnumCooldownType.
     *
     * @param cooldownDuration the duration of the cooldown
     */
    EnumCooldownType(long cooldownDuration) {
        this.cooldownDuration = cooldownDuration;
    }
}