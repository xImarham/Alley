package dev.revere.alley.base.nametag;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
@Getter
public class NametagContext {
    private final Profile viewerProfile;
    private final Profile targetProfile;
    private final Player viewer;
    private final Player target;
    private final Alley plugin;

    public NametagContext(Player viewer, Player target, Alley plugin) {
        this.viewer = viewer;
        this.target = target;
        this.plugin = plugin;
        this.viewerProfile = plugin.getProfileService().getProfile(viewer.getUniqueId());
        this.targetProfile = plugin.getProfileService().getProfile(target.getUniqueId());
    }
}