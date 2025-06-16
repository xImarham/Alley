package dev.revere.alley.base.queue.listener;

import dev.revere.alley.Alley;
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
    protected final Alley plugin;

    /**
     * Constructor for the QueueListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public QueueListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.WAITING) {
            if (profile.getQueueProfile().getQueue() != null) {
                profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.WAITING) {
            if (profile.getQueueProfile().getQueue() != null) {
                profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
            }
        }
    }
}