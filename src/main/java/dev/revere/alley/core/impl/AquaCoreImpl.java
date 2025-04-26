package dev.revere.alley.core.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.core.ICore;
import dev.revere.alley.core.enums.EnumCoreType;
import dev.revere.alley.util.chat.CC;
import me.activated.core.plugin.AquaCoreAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class AquaCoreImpl implements ICore {
    protected final AquaCoreAPI aquaCoreAPI;

    /**
     * Constructor for the AquaCoreImpl class.
     *
     * @param aquaCoreAPI The AquaCoreAPI instance.
     */
    public AquaCoreImpl(AquaCoreAPI aquaCoreAPI) {
        this.aquaCoreAPI = aquaCoreAPI;
    }

    @Override
    public EnumCoreType getType() {
        return EnumCoreType.AQUA;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return this.aquaCoreAPI.getPlayerNameColor(player.getUniqueId());
    }

    @Override
    public String getRankPrefix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getPrefix();
    }

    @Override
    public String getRankSuffix(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getSuffix();
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return this.aquaCoreAPI.getPlayerRank(player.getUniqueId()).getColor();
    }

    @Override
    public String getTagPrefix(Player player) {
        return this.aquaCoreAPI.getTag(player.getUniqueId()).getPrefix();
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return this.aquaCoreAPI.getTag(player.getUniqueId()).getColor();
    }

    @Override
    public String getChatFormat(Player player, String eventMessage, String separator) {
        String prefix = CC.translate(this.getRankPrefix(player));
        String suffix = CC.translate(this.getRankSuffix(player));
        ChatColor color = this.getPlayerColor(player);

        String selectedTitle = CC.translate(Alley.getInstance().getProfileService().getProfile(player.getUniqueId()).getProfileData().getSelectedTitle());

        return prefix + color + player.getName() + suffix + this.aquaCoreAPI.getTagFormat(player) + separator + eventMessage + selectedTitle;
    }
}
