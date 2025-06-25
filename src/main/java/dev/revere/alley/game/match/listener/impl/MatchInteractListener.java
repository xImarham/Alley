package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingLivesImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingParkourImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchInteractListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchInteractListener class.
     *
     * @param plugin The Alley instance
     */
    public MatchInteractListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void handleParkourInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) return;
        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingParkourImpl.class)) return;

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        if (ListenerUtil.notSteppingOnPlate(event)) return;

        AbstractMatch match = profile.getMatch();
        match.getParticipants().forEach(participant -> {
            Player participantPlayer = participant.getPlayer().getPlayer();
            if (participantPlayer != null && !participantPlayer.getUniqueId().equals(player.getUniqueId())) {
                match.handleDeath(participantPlayer);
            }
        });
    }
}