package dev.revere.alley.base.visibility;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 06/06/2025
 */
@Service(provides = IVisibilityService.class, priority = 360)
public class VisibilityService implements IVisibilityService {
    private final Alley plugin;
    private final IProfileService profileService;

    /**
     * Constructor for DI.
     */
    public VisibilityService(Alley plugin, IProfileService profileService) {
        this.plugin = plugin;
        this.profileService = profileService;
    }

    @Override
    public void updateVisibility(Player player) {
        if (player == null || !player.isOnline()) return;

        for (Player otherPlayer : this.plugin.getServer().getOnlinePlayers()) {
            if (player.equals(otherPlayer)) continue;
            handleVisibility(player, otherPlayer);
            handleVisibility(otherPlayer, player);
        }
    }

    /**
     * Hides a player from another player based on their profile states.
     *
     * @param viewer The profile of the viewer.
     * @param target The target player to hide.
     */
    public void handleVisibility(Player viewer, Player target) {
        if (viewer == null || target == null || !viewer.isOnline() || !target.isOnline()) {
            return;
        }

        Profile viewerProfile = this.profileService.getProfile(viewer.getUniqueId());
        Profile targetProfile = this.profileService.getProfile(target.getUniqueId());

        if (viewerProfile == null || targetProfile == null) {
            Logger.error("Could not retrieve profile for viewer or target player.");
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
            default:
                viewer.showPlayer(target);
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
        switch (targetProfileState) {
            case LOBBY:
            case WAITING:
            case EDITING:
                viewer.showPlayer(target);
                break;
            default:
                viewer.hidePlayer(target);
                break;
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
        if (viewerProfile.getMatch() == null) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetProfile.getState() == EnumProfileState.SPECTATING) {
            viewer.hidePlayer(target);
            return;
        }

        if (targetProfile.getMatch() == null || !viewerProfile.getMatch().equals(targetProfile.getMatch())) {
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
        Profile targetProfile = this.profileService.getProfile(target.getUniqueId());
        if (targetProfile.getState() == EnumProfileState.SPECTATING) {
            viewer.showPlayer(target);
            return;
        }

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