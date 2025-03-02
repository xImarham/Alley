package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cooldown.Cooldown;
import dev.revere.alley.feature.cooldown.CooldownRepository;
import dev.revere.alley.feature.cooldown.enums.EnumCooldownType;
import dev.revere.alley.feature.kit.settings.impl.KitSettingLivesImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingParkourImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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
        Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) return;
        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingParkourImpl.class)) return;
        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.STONE_PLATE) return;

        AbstractMatch match = profile.getMatch();
        match.getParticipants().forEach(participant -> {
            Player participantPlayer = participant.getPlayer().getPlayer();
            if (participantPlayer != null && !participantPlayer.getUniqueId().equals(player.getUniqueId())) {
                match.handleDeath(participantPlayer);
            }
        });
    }

    @EventHandler
    private void onPlayerPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (profile.getMatch() == null) return;

        if (profile.getMatch().getState() == EnumMatchState.STARTING && item != null && item.getType() == Material.ENDER_PEARL) {
            event.setCancelled(true);
            player.updateInventory();
            player.sendMessage(CC.translate("&cYou cannot use ender pearls during the starting phase."));
            return;
        }

        if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) return;

        if (profile.getState() == EnumProfileState.PLAYING && item != null && item.getType() == Material.ENDER_PEARL) {
            CooldownRepository cooldownRepository = this.plugin.getCooldownRepository();
            Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL));

            if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using another ender pearl."));
                return;
            }

            Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                Cooldown newCooldown = new Cooldown(EnumCooldownType.ENDER_PEARL, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
                cooldownRepository.addCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL, newCooldown);
                return newCooldown;
            });

            cooldown.resetCooldown();
        }
    }
}