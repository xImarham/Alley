package dev.revere.alley.base.queue.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class QueueListener implements Listener {
    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.WAITING) {
            if (profile.getQueueProfile().getQueue() != null) {
                profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.WAITING) {
            if (profile.getQueueProfile().getQueue() != null) {
                profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
            }
        }
    }
}