package dev.revere.alley.combat;

import dev.revere.alley.util.TimeUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/01/2025
 */
@Getter
public class CombatRepository {
    private final HashMap<Long, Combat> combatMap;
    private final long combatTime;

    public CombatRepository() {
        this.combatMap = new HashMap<>();
        this.combatTime = TimeUtil.secondsToMillis(15);
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
        Combat combat = new Combat(attacker, victim);
        this.combatMap.put(this.combatTime, combat);
    }

    /**
     * Remove a player from combat.
     *
     * @param player The player.
     */
    public void removePlayerFromCombat(UUID player) {
        this.combatMap.values().removeIf(combat -> combat.getAttacker().equals(player) || combat.getVictim().equals(player));
    }

    /**
     * Check if a player is in combat.
     *
     * @param player The player.
     * @return Whether the player is in combat.
     */
    public boolean isPlayerInCombat(UUID player) {
        return this.combatMap.values().stream().anyMatch(combat -> combat.getAttacker().equals(player) || combat.getVictim().equals(player));
    }

    /**
     * Check if a player is expired from combat.
     *
     * @param player The player.
     * @return Whether the player is expired.
     */
    public boolean isExpired(UUID player) {
        return this.combatMap.entrySet().stream()
                .filter(entry -> entry.getValue().getAttacker().equals(player) || entry.getValue().getVictim().equals(player))
                .anyMatch(entry -> System.currentTimeMillis() - entry.getKey() >= this.combatTime);
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
                .mapToLong(entry -> this.combatTime - (System.currentTimeMillis() - entry.getKey()))
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
        return TimeUtil.millisToSeconds(remainingTime) + "s";
    }
}