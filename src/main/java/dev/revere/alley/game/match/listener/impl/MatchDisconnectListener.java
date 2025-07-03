package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
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
 * @project Alley
 * @since 08/02/2025
 */
public class MatchDisconnectListener implements Listener {
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING || profile.getState() == EnumProfileState.SPECTATING) {
            profile.setState(EnumProfileState.LOBBY);
            AbstractMatch match = profile.getMatch();
            if (match.getSpectators().contains(player.getUniqueId())) {
                match.removeSpectator(player, true);
            }

            if (match.getGamePlayer(player) == null) {
                return;
            }

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            AbstractMatch match = profile.getMatch();

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }
}