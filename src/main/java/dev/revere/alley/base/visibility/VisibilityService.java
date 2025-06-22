package dev.revere.alley.base.visibility;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/06/2025
 */
public class VisibilityService {
    protected final Alley plugin;

    /**
     * Constructor for the VisibilityService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public VisibilityService(Alley plugin) {
        this.plugin = plugin;
    }

    /**
     * Updates the visibility of a player based on their profile state.
     * When a player's state changes, we need to update visibility bidirectionally:
     * 1. How all other players see this player
     * 2. How this player sees all other players
     *
     * @param player The player whose visibility is to be updated.
     */
    public void updateVisibility(Player player) {
        this.plugin.getServer().getOnlinePlayers().stream()
                .filter(otherPlayer -> !otherPlayer.equals(player))
                .forEach(otherPlayer -> {
                    this.handleVisibility(otherPlayer, player);
                    this.handleVisibility(player, otherPlayer);
                });
    }

    /**
     * Hides a player from another player based on their profile states.
     *
     * @param viewer The profile of the viewer.
     * @param target The target player to hide.
     */
    public void handleVisibility(Player viewer, Player target) {
        if (target == null || viewer == null || target.equals(viewer)) {
            Logger.logError("Something went wrong while hiding player. Either no players are online or target is null.");
            return;
        }

        Profile viewerProfile = this.plugin.getProfileService().getProfile(viewer.getUniqueId());
        Profile targetProfile = this.plugin.getProfileService().getProfile(target.getUniqueId());

        if (viewerProfile == null || targetProfile == null) {
            Logger.logError("Could not retrieve profile for viewer or target player.");
            return;
        }

        EnumProfileState viewerProfileState = viewerProfile.getState();
        EnumProfileState targetProfileState = targetProfile.getState();

        switch (viewerProfileState) {
            case LOBBY:
            case WAITING:
                this.handleLobbyAndQueueState(viewer, target, targetProfileState);
                break;
            case FFA:
                this.handleFfaState(viewer, target, targetProfileState);
                break;
            case PLAYING:
                this.handlePlayingCase(viewer, target, viewerProfile, targetProfile);
                break;
            case SPECTATING:
                this.handleSpectatingCase(viewer, target, viewerProfile);
                break;
        }
    }

    /**
     * Handles the visibility logic for players in lobby and queue states.
     *
     * @param viewer             the player who is viewing.
     * @param target             player to be viewed.
     * @param targetProfileState the profile state of the target player.
     */
    private void handleLobbyAndQueueState(Player viewer, Player target, EnumProfileState targetProfileState) {
        if (targetProfileState == EnumProfileState.LOBBY
                || targetProfileState == EnumProfileState.EDITING
                || targetProfileState == EnumProfileState.WAITING) {
            viewer.showPlayer(target);
        } else {
            viewer.hidePlayer(target);
        }

        //TODO: further logic once we implement /tpv command (TogglePlayerVisibility)
    }

    private void handleFfaState(Player viewer, Player target, EnumProfileState targetProfileState) {
        if (targetProfileState == EnumProfileState.FFA) {
            viewer.showPlayer(target);
        } else {
            viewer.hidePlayer(target);
        }
    }

    /**
     * Handles the visibility logic for players in playing state.
     *
     * @param viewer        The player who is viewing.
     * @param target        The target player to be viewed.
     * @param viewerProfile The profile of the viewer.
     */
    private void handlePlayingCase(Player viewer, Player target, Profile viewerProfile, Profile targetProfile) {
        if (viewerProfile.getMatch() == null || targetProfile.getMatch() == null) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetProfile.getState() == EnumProfileState.SPECTATING) {
            viewer.hidePlayer(target);
            return;
        }

        MatchGamePlayerImpl targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (targetProfile.getMatch().getSpectators().contains(target.getUniqueId())) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetGamePlayer.isEliminated()) {
            viewer.hidePlayer(target);
            return;
        }

        viewer.showPlayer(target);
    }


    /**
     * Handles the visibility logic for players in spectating state.
     *
     * @param viewer        The player who is viewing.
     * @param target        The target player to be viewed.
     * @param viewerProfile The profile of the viewer.
     */
    private void handleSpectatingCase(Player viewer, Player target, Profile viewerProfile) {
        if (viewerProfile.getMatch() == null) {
            viewer.hidePlayer(target);
            return;
        }

        MatchGamePlayerImpl spectatingTargetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (spectatingTargetGamePlayer == null) {
            viewer.hidePlayer(target);
            return;
        }

        viewer.showPlayer(target);
    }
}