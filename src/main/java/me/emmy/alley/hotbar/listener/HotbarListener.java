package me.emmy.alley.hotbar.listener;

import me.emmy.alley.hotbar.LobbyItem;
import me.emmy.alley.hotbar.PartyItem;
import me.emmy.alley.hotbar.QueueItem;
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

        LobbyItem lobbyItem = LobbyItem.getByItemStack(event.getItem());
        if (lobbyItem != null) {
            handleLobbyItemInteract(event, player, lobbyItem);
            return;
        }

        QueueItem queueItem = QueueItem.getByItemStack(event.getItem());
        if (queueItem != null) {
            handleQueueItemInteract(event, player, queueItem);
            return;
        }

        PartyItem partyItem = PartyItem.getByItemStack(event.getItem());
        if (partyItem != null) {
            handlePartyItemInteract(event, player, partyItem);
        }
    }

    /**
     * Handles the player's interaction with the lobby items.
     *
     * @param event     The event to handle.
     * @param player    The player to handle.
     * @param lobbyItem The lobby item to handle.
     */
    private void handleLobbyItemInteract(PlayerInteractEvent event, Player player, LobbyItem lobbyItem) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (lobbyItem.getCommand() != null) {
            player.performCommand(lobbyItem.getCommand());
        }
    }

    /**
     * Handles the player's interaction with the queue items.
     *
     * @param event     The event to handle.
     * @param player    The player to handle.
     * @param queueItem The queue item to handle.
     */
    private void handleQueueItemInteract(PlayerInteractEvent event, Player player, QueueItem queueItem) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (queueItem.getCommand() != null) {
            player.performCommand(queueItem.getCommand());
        }
    }

    /**
     * Handles the player's interaction with the party items.
     *
     * @param event     The event to handle.
     * @param player    The player to handle.
     * @param partyItem The party item to handle.
     */
    private void handlePartyItemInteract(PlayerInteractEvent event, Player player, PartyItem partyItem) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (partyItem.getCommand() != null) {
            player.performCommand(partyItem.getCommand());
        }
    }
}
