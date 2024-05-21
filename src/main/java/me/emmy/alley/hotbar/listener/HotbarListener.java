package me.emmy.alley.hotbar.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.HotbarItem;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HotbarListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getItem() == null) {
            return;
        }

        handleLobbyInteract(event, player);
    }

    /**
     * Handles the player's interaction with the lobby items.
     *
     * @param event  The event to handle.
     * @param player The player to handle.
     */
    private void handleLobbyInteract(PlayerInteractEvent event, Player player) {
        HotbarItem clickedItem = HotbarItem.getByItemStack(event.getItem());
        if (clickedItem == null) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (clickedItem.getCommand() != null) {
            player.performCommand(clickedItem.getCommand());
        } else {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

            if (clickedItem == HotbarItem.LEAVE_QUEUE) {
                if (profile.getState() == EnumProfileState.WAITING) {
                    profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
                }
            }
        }
    }
}
