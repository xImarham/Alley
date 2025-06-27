package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 27/06/2025
 */
public class MatchPotionListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchPotionListener class.
     *
     * @param plugin The Alley instance
     */
    public MatchPotionListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPotionSplashEvent(PotionSplashEvent event) {
        if (!(event.getPotion().getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) event.getPotion().getShooter();
        Profile profile = this.plugin.getProfileService().getProfile(shooter.getUniqueId());

        EnumProfileState profileState = profile.getState();
        if (profileState != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null || match.getState() != EnumMatchState.RUNNING) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = match.getGamePlayer(shooter);

        if (event.getIntensity(shooter) <= 0.5D) {
            gamePlayer.getData().incrementMissedPotions();
        }
    }

    @EventHandler
    private void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof ThrownPotion)) {
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();
        Profile profile = this.plugin.getProfileService().getProfile(shooter.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null || match.getState() != EnumMatchState.RUNNING) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = match.getGamePlayer(shooter);
        gamePlayer.getData().incrementThrownPotions();
    }
}