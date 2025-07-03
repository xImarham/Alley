package dev.revere.alley.server.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.IPluginConstant;
import dev.revere.alley.profile.progress.IProgressService;
import dev.revere.alley.server.ICore;
import dev.revere.alley.server.enums.EnumCoreType;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class DefaultCoreImpl implements ICore {
    protected final Alley plugin;
    protected final String adminPermission;

    /**
     * Constructor for the DefaultCoreImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public DefaultCoreImpl(Alley plugin) {
        this.plugin = plugin;
        this.adminPermission = Alley.getInstance().getService(IPluginConstant.class).getAdminPermissionPrefix();
    }

    @Override
    public EnumCoreType getType() {
        return EnumCoreType.DEFAULT;
    }

    @Override
    public ChatColor getPlayerColor(Player player) {
        if (player.isOp()) {
            return ChatColor.DARK_RED;
        } else if (player.hasPermission(this.adminPermission)) {
            return ChatColor.RED;
        }

        return ChatColor.GREEN;
    }

    @Override
    public String getRankPrefix(Player player) {
        if (player.isOp()) {
            return CC.translate("&7[&4&oOwner&7] &4");
        } else if (player.hasPermission(this.adminPermission)) {
            return CC.translate("&7[&c&oAdmin&7] &c");
        }

        return CC.translate("&a");
    }

    @Override
    public String getRankSuffix(Player player) {
        return "";
    }

    @Override
    public ChatColor getRankColor(Player player) {
        if (player.isOp()) {
            return ChatColor.DARK_RED;
        } else if (player.hasPermission(this.adminPermission)) {
            return ChatColor.RED;
        }

        return ChatColor.GREEN;
    }

    @Override
    public String getTagPrefix(Player player) {
        return "";
    }

    @Override
    public ChatColor getTagColor(Player player) {
        return ChatColor.WHITE;
    }
}