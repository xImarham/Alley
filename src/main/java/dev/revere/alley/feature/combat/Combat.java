package dev.revere.alley.feature.combat;

import lombok.Getter;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/01/2025
 */
@Getter
public class Combat {
    private final UUID attacker;
    private final UUID victim;

    /**
     * Constructor for the Combat class.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     */
    public Combat(UUID attacker, UUID victim) {
        this.attacker = attacker;
        this.victim = victim;
    }
}