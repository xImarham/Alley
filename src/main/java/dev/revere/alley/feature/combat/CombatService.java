package dev.revere.alley.feature.combat;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.TimeUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emmy
 * @project Alley
 * @since 07/03/2025
 */
@Getter
public class CombatService {
    private final Map<UUID, Combat> combatMap;

    private final long ffaExpirationTime;
    private final long defaultExpirationTime;

    public CombatService() {
        this.combatMap = new ConcurrentHashMap<>();
        this.ffaExpirationTime = 15 * 1000L; // 15 seconds
        this.defaultExpirationTime = 5 * 1000L; // 5 seconds
    }

    /**
     * Records the last attacker of a player and sets the expiration time based on the player's state.
     *
     * @param victim   The player who was attacked.
     * @param attacker The player who attacked.
     */
    public void setLastAttacker(Player victim, Player attacker) {
        EnumProfileState profileState = Alley.getInstance().getProfileService().getProfile(victim.getUniqueId()).getState();

        long expirationTime = (profileState == EnumProfileState.FFA) ? this.ffaExpirationTime : this.defaultExpirationTime;

        Combat victimCombatData = new Combat(attacker.getUniqueId(), System.currentTimeMillis(), expirationTime);
        this.combatMap.put(victim.getUniqueId(), victimCombatData);

        Combat attackerCombatData = new Combat(victim.getUniqueId(), System.currentTimeMillis(), expirationTime);
        this.combatMap.put(attacker.getUniqueId(), attackerCombatData);
    }

    /**
     * Gets the last attacker of a player, considering expiration.
     *
     * @param victim The player who was attacked.
     * @return The last attacker, or null if expired/not found.
     */
    public Player getLastAttacker(Player victim) {
        Combat combat = this.combatMap.get(victim.getUniqueId());
        if (combat == null) return null;

        long attackTime = combat.getAttackTimestamp();
        long expirationTime = combat.getExpirationTime();

        if (System.currentTimeMillis() - attackTime > expirationTime) {
            this.removeLastAttacker(victim, false);
            return null;
        }

        return Bukkit.getPlayer(combat.getAttackerUUID());
    }

    /**
     * Resets the combat log of a player.
     *
     * @param player The player.
     */
    public void resetCombatLog(Player player) {
        this.removeLastAttacker(player, false);
    }

    /**
     * Checks if a player is in combat.
     *
     * @param uuid The player to check.
     * @return If the player is in combat.
     */
    public boolean isPlayerInCombat(UUID uuid) {
        if (this.isExpired(Bukkit.getPlayer(uuid))) {
            return false;
        }

        return this.combatMap.containsKey(uuid);
    }

    /**
     * Removes the last attacker record of a player.
     *
     * @param player     The player.
     * @param removeBoth If true, removes both the attacker and the victim.
     */
    public void removeLastAttacker(Player player, boolean removeBoth) {
        Combat combat = this.combatMap.get(player.getUniqueId());
        if (combat != null) {
            this.combatMap.remove(player.getUniqueId());

            if (removeBoth) {
                UUID victimUUID = combat.getAttackerUUID();
                Combat victimCombat = this.combatMap.get(victimUUID);
                if (victimCombat != null) {
                    this.combatMap.remove(victimUUID);
                }
            }
        }
    }

    /**
     * Checks if the last attacker record of a player is expired.
     *
     * @param player The player.
     * @return If the record is expired.
     */
    public boolean isExpired(Player player) {
        Combat combat = this.combatMap.get(player.getUniqueId());
        if (combat == null) return true;

        long attackTime = combat.getAttackTimestamp();
        long expirationTime = combat.getExpirationTime();

        if (System.currentTimeMillis() - attackTime > expirationTime) {
            this.removeLastAttacker(player, false);
            return true;
        }

        return false;
    }

    /**
     * Get the remaining time before the last attacker record expires.
     *
     * @param victim The player.
     * @return The remaining time in milliseconds, or 0 if expired/not found.
     */
    public long getRemainingTime(Player victim) {
        Combat combat = this.combatMap.get(victim.getUniqueId());
        if (combat == null) return 0;

        long attackTime = combat.getAttackTimestamp();
        long expirationTime = combat.getExpirationTime();

        long remaining = (attackTime + expirationTime) - System.currentTimeMillis();
        return Math.max(remaining, 0);
    }

    /**
     * Get the remaining time formatted as a string.
     *
     * @param victim The player.
     * @return The remaining time formatted in seconds.
     */
    public String getRemainingTimeFormatted(Player victim) {
        return TimeUtil.millisToSecondsTimer(this.getRemainingTime(victim)) + "s";
    }
}