package me.emmy.alley.hotbar.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.LobbyItem;
import me.emmy.alley.hotbar.PartyItem;
import me.emmy.alley.hotbar.QueueItem;
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

        handleLobbyItemInteract(event, player);
        handleQueueItemInteract(event, player);
        handePartyItemInteract(event, player);
    }

    /**
     * Handles the player's interaction with the lobby items.
     *
     * @param event  The event to handle.
     * @param player The player to handle.
     */
    private void handleLobbyItemInteract(PlayerInteractEvent event, Player player) {
        LobbyItem lobbyItem = LobbyItem.getByItemStack(event.getItem());

        if (lobbyItem == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (lobbyItem.getCommand() != null) {
            player.performCommand(lobbyItem.getCommand());
        }
    }

    private void handleQueueItemInteract(PlayerInteractEvent event, Player player) {
        QueueItem queueItem = QueueItem.getByItemStack(event.getItem());

        if (queueItem == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (queueItem.getCommand() != null) {
            player.performCommand(queueItem.getCommand());
        }
    }

    private void handePartyItemInteract(PlayerInteractEvent event, Player player) {
        PartyItem partyItem = PartyItem.getByItemStack(event.getItem());

        if (partyItem == null) {
            return;
        }

        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (partyItem.getCommand() != null) {
            player.performCommand(partyItem.getCommand());
        }
    }
}
