package dev.revere.alley.game.party.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.game.party.PartyHandler;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
public class PartyListener implements Listener {

    @EventHandler
    private void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(event.getPlayer().getUniqueId());
        PartyHandler partyHandler = Alley.getInstance().getPartyHandler();
        
        if (profile.getProfileData().getProfileSettingData().getChatChannel().equalsIgnoreCase(EnumChatChannel.PARTY.toString())) {
            event.setCancelled(true);
            if (profile.getParty() == null) {
                event.getPlayer().sendMessage(CC.translate("&cYou're not in a party."));
                return;
            }

            if (!profile.getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                event.getPlayer().sendMessage(CC.translate("&cYou have party messages disabled."));
                return;
            }

            String partyMessage = partyHandler.getChatFormat().replace("{player}", event.getPlayer().getName()).replace("{message}", event.getMessage());
            partyHandler.notifyParty(profile.getParty(), partyMessage);
            return;
        }

        if (event.getMessage().startsWith("#") || event.getMessage().startsWith("!")) {
            if (profile.getParty() == null) {
                event.getPlayer().sendMessage(CC.translate("&cYou're not in a party."));
                return;
            }

            event.setCancelled(true);
            if (!profile.getProfileData().getProfileSettingData().isPartyMessagesEnabled()) {
                event.getPlayer().sendMessage(CC.translate("&cYou have party messages disabled."));
                return;
            }

            String partyMessage = partyHandler.getChatFormat().replace("{player}", event.getPlayer().getName()).replace("{message}", event.getMessage().substring(1));
            partyHandler.notifyParty(profile.getParty(), partyMessage);
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        Party party = profile.getParty();
        if (party == null) {
            return;
        }

        if (party.getLeader() == player) {
            Alley.getInstance().getPartyHandler().disbandParty(player);
            return;
        }

        Alley.getInstance().getPartyHandler().leaveParty(player);
    }
}