package dev.revere.alley.core.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.core.ICore;
import dev.revere.alley.core.enums.EnumCoreType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import services.plasma.helium.api.HeliumAPI;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class HeliumCoreImpl implements ICore {
    protected final HeliumAPI heliumAPI;

    /**
     * Constructor for the HeliumCoreImpl class.
     *
     * @param heliumAPI The HeliumAPI instance.
     */
    public HeliumCoreImpl(HeliumAPI heliumAPI) {
        this.heliumAPI = heliumAPI;
    }

    @Override
    public EnumCoreType getType() {
        return EnumCoreType.HELIUM;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        return ChatColor.valueOf(this.heliumAPI.getRankColor(player.getUniqueId()));
    }

    @Override
    public String getRankPrefix(Player player) {
        return this.heliumAPI.getRankPrefix(player.getUniqueId());
    }

    @Override
    public String getRankSuffix(Player player) {
        return this.heliumAPI.getRankSuffix(player.getUniqueId());
    }

    @Override
    public ChatColor getRankColor(Player player) {
        return ChatColor.valueOf(this.heliumAPI.getRankColor(player.getUniqueId()));
    }

    @Override
    public String getTagPrefix(Player player) {
        return this.heliumAPI.getTagDisplayName(player.getUniqueId());
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return ChatColor.WHITE;
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
}
