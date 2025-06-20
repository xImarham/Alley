package dev.revere.alley.game.ffa.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.ffa.enums.EnumFFAState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.cuboid.Cuboid;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
public class FFACuboidListener implements Listener {
    protected final Alley plugin;
    private final Map<UUID, Boolean> playerStates;

    /**
     * Constructor for the CuboidBoundaryListener.
     *
     * @param plugin The instance of the Alley plugin.
     */
    public FFACuboidListener(Alley plugin) {
        this.plugin = plugin;
        this.playerStates = new HashMap<>();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Cuboid cuboid = this.plugin.getFfaSpawnService().getCuboid();
        if (cuboid == null) {
            return;
        }

        for (AbstractFFAMatch ffaMatch : this.plugin.getFfaService().getMatches()) {
            if (ffaMatch.getPlayers().isEmpty()) {
                return;
            }
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        Profile profile = this.plugin.getProfileService().getProfile(playerId);
        if (profile == null || profile.getState() != EnumProfileState.FFA) {
            return;
        }

        boolean isInCuboid = cuboid.isIn(player);
        boolean wasInCuboid = this.playerStates.getOrDefault(playerId, true);

        if (isInCuboid != wasInCuboid) {
            if (isInCuboid) {
                if (this.plugin.getCombatService().isPlayerInCombat(playerId)) return;
                player.sendMessage(CC.translate("&aYou have entered the FFA spawn area."));
                this.plugin.getFfaService().getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(EnumFFAState.SPAWN));
            } else {
                player.sendMessage(CC.translate("&cYou have left the FFA spawn area."));
                this.plugin.getFfaService().getMatchByPlayer(player).ifPresent(match -> match.getGameFFAPlayer(player).setState(EnumFFAState.FIGHTING));
            }

            this.playerStates.put(playerId, isInCuboid);
        }
    }
}