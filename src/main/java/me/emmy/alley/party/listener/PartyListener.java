package me.emmy.alley.party.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.utils.chat.CC;
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

        if (event.getMessage().startsWith("#") || event.getMessage().startsWith("!")) {
            if (profile.getParty() == null) {
                event.getPlayer().sendMessage(CC.translate("&cYou're not in a party."));
                return;
            }

            event.setCancelled(true);
            profile.getParty().notifyParty(CC.translate("&7[&aParty&7] &a" + event.getPlayer().getName() + "&7: &f" + event.getMessage().substring(1)));
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getParty() != null) {
            if (profile.getParty().getLeader().equals(player)) {
                profile.getParty().disband();
                return;
            }

            profile.getParty().leaveParty(player);
        }
    }
}
