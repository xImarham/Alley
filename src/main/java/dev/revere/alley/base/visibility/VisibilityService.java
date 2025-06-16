package dev.revere.alley.base.visibility;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
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
     *
     * @param player The player whose visibility is to be updated.
     */
    public void updateVisibility(Player player) {
        this.plugin.getServer().getOnlinePlayers().forEach(player1 ->
                this.hidePlayer(player, player1)
        );
    }

    /**
     * Updates the visibility of all players for a specific player.
     *
     * @param player The player whose visibility is to be updated for all others.
     */
    public void updateVisibilityAll(Player player) {
        for (Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
            this.hidePlayer(player, onlinePlayer);
            this.hidePlayer(onlinePlayer, player);
        }
    }

    /**
     * Hides a player from another player based on their profile states.
     *
     * @param viewer The profile of the viewer.
     * @param target The target player to hide.
     */
    public void hidePlayer(Player viewer, Player target) {
        Profile viewerProfile = this.plugin.getProfileService().getProfile(viewer.getUniqueId());
        Profile targetProfile = this.plugin.getProfileService().getProfile(target.getUniqueId());

        EnumProfileState viewerProfileState = viewerProfile.getState();
        EnumProfileState targetProfileState = targetProfile.getState();

        switch (viewerProfileState) {
            case LOBBY:
            case WAITING:
                this.handleLobbyAndQueueState(viewer, target, targetProfileState);
                break;
            case PLAYING:
                this.handlePlayingCase(viewer, target, viewerProfile);
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
        if (targetProfileState == EnumProfileState.FFA) {
            viewer.hidePlayer(target);
        }

        //TODO: further logic once we implement /tpv command (TogglePlayerVisibility)
    }

    /**
     * Handles the visibility logic for players in playing state.
     *
     * @param viewer        The player who is viewing.
     * @param target        The target player to be viewed.
     * @param viewerProfile The profile of the viewer.
     */
    private void handlePlayingCase(Player viewer, Player target, Profile viewerProfile) {
        MatchGamePlayerImpl targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (targetGamePlayer != null) {
            if (!targetGamePlayer.isDead()) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }
        } else {
            viewer.hidePlayer(target);
        }
    }

    /**
     * Handles the visibility logic for players in spectating state.
     *
     * @param viewer        The player who is viewing.
     * @param target        The target player to be viewed.
     * @param viewerProfile The profile of the viewer.
     */
    private void handleSpectatingCase(Player viewer, Player target, Profile viewerProfile) {
        MatchGamePlayerImpl spectatingTargetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);
        if (spectatingTargetGamePlayer != null) {
            if (!spectatingTargetGamePlayer.isDead() && !spectatingTargetGamePlayer.isDisconnected()) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }
        } else {
            viewer.hidePlayer(target);
        }
    }
}