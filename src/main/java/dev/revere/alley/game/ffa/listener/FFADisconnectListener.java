package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.ICombatService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Emmy
 * @project alley-practice
 * @since 16/07/2025
 */
public class FFADisconnectListener implements Listener {
    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.FFA) return;

        ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.FFA) return;

        ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }
}