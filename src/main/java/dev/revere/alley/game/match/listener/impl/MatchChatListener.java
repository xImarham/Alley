package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.MatchService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public class MatchChatListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchChatListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MatchChatListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onCommandPreProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission(this.plugin.getPluginConstant().getAdminPermissionPrefix() + ".bypass.command.restriction")) {
            return;
        }

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        AbstractMatch match = profile.getMatch();
        if (match == null) return;

        MatchService matchService = this.plugin.getMatchService();
        String commandInput = event.getMessage().toLowerCase();

        for (String blockedCommand : matchService.getBlockedCommands()) {
            if (commandInput.startsWith("/" + blockedCommand.toLowerCase())) {
                event.getPlayer().sendMessage(CC.translate("&cThis command is blocked during matches."));
                event.setCancelled(true);
                return;
            }
        }
    }
}