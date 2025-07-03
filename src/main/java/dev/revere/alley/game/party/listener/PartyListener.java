package dev.revere.alley.game.party.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.party.IPartyService;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyListener implements Listener {
    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        IPartyService partyService = Alley.getInstance().getService(IPartyService.class);

        Profile profile = profileService.getProfile(event.getPlayer().getUniqueId());

        if (profile.getProfileData().getSettingData().getChatChannel().equalsIgnoreCase(EnumChatChannel.PARTY.toString())) {
            event.setCancelled(true);
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cYou're not in a party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cYou have party messages disabled."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage());
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
            return;
        }

        if (event.getMessage().startsWith("#") || event.getMessage().startsWith("!")) {
            if (profile.getParty() == null) {
                player.sendMessage(CC.translate("&cYou're not in a party."));
                event.setCancelled(true);
                return;
            }

            if (!profile.getProfileData().getSettingData().isPartyMessagesEnabled()) {
                player.sendMessage(CC.translate("&cYou have party messages disabled."));
                event.setCancelled(true);
                return;
            }

            String partyMessage = partyService.getChatFormat().replace("{player}", player.getName()).replace("{message}", event.getMessage().substring(1));
            profile.getParty().notifyParty(partyMessage);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            Alley.getInstance().getService(IPartyService.class).disbandParty(player);
            return;
        }

        Alley.getInstance().getService(IPartyService.class).leaveParty(player);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            Alley.getInstance().getService(IPartyService.class).disbandParty(player);
            return;
        }

        Alley.getInstance().getService(IPartyService.class).leaveParty(player);
    }
}