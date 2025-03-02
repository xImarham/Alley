package dev.revere.alley.feature.combat;

import dev.revere.alley.util.TimeUtil;
import lombok.Getter;
import lombok.var;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/01/2025
 */
@Getter
public class CombatService {
    private final HashMap<Long, Combat> combatMap;
    private final long combatTime;

    public CombatService() {
        this.combatMap = new HashMap<>();
        this.combatTime = 15 * 1000L;
    }

    /**
     * Get the combat of a player.
     *
     * @param player The player.
     * @return The combat.
     */
    public Combat getCombat(UUID player) {
        return this.combatMap.values().stream()
                .filter(combat -> combat.getAttacker().equals(player) || combat.getVictim().equals(player))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add players to combat.
     *
     * @param attacker The attacker.
     * @param victim   The victim.
     */
    public void addPlayersToCombat(UUID attacker, UUID victim) {
        if (this.isPlayerInCombat(attacker) || this.isPlayerInCombat(victim)) {
            this.resetCombat(attacker);
        }

        Combat combat = new Combat(attacker, victim);
        this.combatMap.put(System.currentTimeMillis(), combat);
    }

    /**
     * Check if a player is in combat.
     *
     * @param player The player.
     * @return Whether the player is in combat.
     */
    public boolean isPlayerInCombat(UUID player) {
        for (var entry : this.combatMap.entrySet()) {
            Combat combat = entry.getValue();

            if ((combat.getAttacker().equals(player) || combat.getVictim().equals(player)) && this.isExpired(entry.getKey())) {
                this.resetCombat(player);
                return false;
            }

            if (combat.getAttacker().equals(player) || combat.getVictim().equals(player)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if the combat is expired.
     *
     * @param timestamp The timestamp.
     * @return Whether the combat is expired.
     */
    public boolean isExpired(Long timestamp) {
        return System.currentTimeMillis() - timestamp >= this.combatTime;
    }

    /**
     * Reset the combat of a player.
     *
     * @param player The player.
     */
    public void resetCombat(UUID player) {
        this.combatMap.values().removeIf(combat -> combat.getAttacker().equals(player) || combat.getVictim().equals(player));
    }

    /**
     * Get the remaining time of a player in combat.
     *
     * @param player The player.
     * @return The remaining time.
     */
    public long getRemainingTime(UUID player) {
        return this.combatMap.entrySet().stream()
                .filter(entry -> entry.getValue().getAttacker().equals(player) || entry.getValue().getVictim().equals(player))
                .mapToLong(entry -> (entry.getKey() + this.combatTime) - System.currentTimeMillis())
                .findFirst()
                .orElse(0);
    }

    /**
     * Get the remaining time of a player in combat formatted.
     *
     * @param player The player.
     * @return The remaining time formatted.
     */
    public String getRemainingTimeFormatted(UUID player) {
        long remainingTime = this.getRemainingTime(player);
        return TimeUtil.millisToSecondsRaw(remainingTime) + "s";
    }
}