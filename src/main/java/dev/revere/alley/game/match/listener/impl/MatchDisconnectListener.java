package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
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
    protected final Alley plugin;

    /**
     * Constructor for the MatchDisconnectListener class.
     *
     * @param plugin The Alley instance
     */
    public MatchDisconnectListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            profile.setState(EnumProfileState.LOBBY);
            AbstractMatch match = profile.getMatch();

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                if (handleUniqueKitCases(match, player)) return;
                match.handleDisconnect(player);
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            AbstractMatch match = profile.getMatch();

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                if (handleUniqueKitCases(match, player)) return;
                match.handleDisconnect(player);
            }
        }
    }

    /**
     * Handles unique kit cases when a player disconnects.
     *
     * @param match  The match.
     * @param player The player.
     * @return Whether the player was handled.
     */
    private boolean handleUniqueKitCases(AbstractMatch match, Player player) {
        if (match.getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
            match.getGamePlayer(player).getData().setLives(0);
            match.handleDeath(player);
            return true;
        } else if (match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) {
            //TODO: handle battle rush disconnect, couldnt be bothered to implement it right now
            return true;
        }
        return false;
    }
}