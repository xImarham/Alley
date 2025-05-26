package dev.revere.alley.base.combat.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.CombatService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 14/03/2025
 */
public class CombatListener implements Listener {
    protected final Alley alley;

    public CombatListener(Alley alley) {
        this.alley = alley;
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.removeFromCombatMap(player);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        this.removeFromCombatMap(player);
    }

    /**
     * Removes the player from the combat map.
     *
     * @param player The player to remove.
     */
    private void removeFromCombatMap(Player player) {
        CombatService combatService = this.alley.getCombatService();
        if (combatService.getCombatMap().containsKey(player.getUniqueId())) {
            combatService.removeLastAttacker(player, true);
        }
    }
}