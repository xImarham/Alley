package dev.revere.alley.core.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.core.ICore;
import dev.revere.alley.core.enums.EnumCoreType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import xyz.refinedev.phoenix.SharedAPI;
import xyz.refinedev.phoenix.profile.Profile;

import java.util.Objects;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class PhoenixCoreImpl implements ICore {
    protected final SharedAPI phoenixAPI;

    /**
     * Constructor for the PhoenixCoreImpl class.
     *
     * @param phoenixAPI The Shared Phoenix API instance.
     */
    public PhoenixCoreImpl(SharedAPI phoenixAPI) {
        this.phoenixAPI = phoenixAPI;
    }

    @Override
    public EnumCoreType getType() {
        return EnumCoreType.PHOENIX;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return ChatColor.valueOf(this.getProfile(player).getHighestRank().getColor());
    }

    @Override
    public String getRankPrefix(Player player) {
        return this.getProfile(player).getHighestRank().getPrefix();
    }

    @Override
    public String getRankSuffix(Player player) {
        return this.getProfile(player).getHighestRank().getSuffix();
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return ChatColor.valueOf(this.getProfile(player).getHighestRank().getColor());
    }

    @Override
    public String getTagPrefix(Player player) {
        return Objects.requireNonNull(this.getProfile(player).getTag()).getPrefix();
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return ChatColor.valueOf(Objects.requireNonNull(this.getProfile(player).getTag()).getColor());
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        String prefix = CC.translate(this.getRankPrefix(player));
        String suffix = CC.translate(this.getRankSuffix(player));
        String tagPrefix = CC.translate(this.getTagPrefix(player));

        ChatColor color = this.getPlayerColor(player);
        ChatColor rankColor = this.getRankColor(player);
        ChatColor tagColor = this.getTagColor(player);

        String selectedTitle = CC.translate(Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getProfileData().getSelectedTitle());

        return prefix + rankColor + color + player.getName() + suffix + tagColor + tagPrefix + separator + eventMessage + selectedTitle;
    }

    /**
     * Retrieves the Profile object associated with the given player.
     *
     * @param player The player whose profile is to be retrieved.
     * @return The Profile object associated with the player.
     */
    private Profile getProfile(Player player) {
        return this.phoenixAPI.getProfileHandler().getProfile(player.getUniqueId());
    }
}