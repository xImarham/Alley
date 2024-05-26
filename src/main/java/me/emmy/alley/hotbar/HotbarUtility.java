package me.emmy.alley.hotbar;

import me.emmy.alley.hotbar.enums.LobbyItem;
import me.emmy.alley.hotbar.enums.PartyItem;
import me.emmy.alley.hotbar.enums.QueueItem;
import me.emmy.alley.hotbar.enums.SpectatorItem;
import me.emmy.alley.utils.PlayerUtil;
import org.bukkit.entity.Player;

public class HotbarUtility {

    /**
     * Applies the spawn items to the player's inventory.
     *
     * @param player The player to apply the spawn items to.
     */
    public void applySpawnItems(Player player) {
        PlayerUtil.reset(player);
        player.getInventory().setItem(LobbyItem.QUEUES.getSlot(), LobbyItem.QUEUES.constructItem());
        player.getInventory().setItem(LobbyItem.CURRENT_MATCHES.getSlot(), LobbyItem.CURRENT_MATCHES.constructItem());
        player.getInventory().setItem(LobbyItem.KIT_EDITOR.getSlot(), LobbyItem.KIT_EDITOR.constructItem());
        player.getInventory().setItem(LobbyItem.PARTY.getSlot(), LobbyItem.PARTY.constructItem());
        player.getInventory().setItem(LobbyItem.LEADERBOARD.getSlot(), LobbyItem.LEADERBOARD.constructItem());
        player.getInventory().setItem(LobbyItem.EVENTS.getSlot(), LobbyItem.EVENTS.constructItem());
        player.getInventory().setItem(LobbyItem.SETTINGS.getSlot(), LobbyItem.SETTINGS.constructItem());
        player.updateInventory();
    }

    /**
     * Applies the spectator items to the player's inventory.
     *
     * @param player The player to apply the spectator items to.
     */
    public void applySpectatorItems(Player player) {
        PlayerUtil.reset(player);
        player.getInventory().setItem(SpectatorItem.STOP_WATCHING.getSlot(), SpectatorItem.STOP_WATCHING.constructItem());
        player.updateInventory();
    }

    /**
     * Applies the queue items to the player's inventory.
     *
     * @param player The player to apply the queue items to.
     */
    public void applyQueueItems(Player player) {
        PlayerUtil.reset(player);
        player.getInventory().setItem(QueueItem.LEAVE_QUEUE.getSlot(), QueueItem.LEAVE_QUEUE.constructItem());
        player.updateInventory();
    }

    public void applyPartyItems(Player player) {
        PlayerUtil.reset(player);
        player.getInventory().setItem(PartyItem.DUO_UNRANKED_QUEUE.getSlot(), PartyItem.DUO_UNRANKED_QUEUE.constructItem());
        player.getInventory().setItem(PartyItem.KIT_EDITOR.getSlot(), PartyItem.KIT_EDITOR.constructItem());
        player.getInventory().setItem(PartyItem.START_PARTY_EVENT.getSlot(), PartyItem.START_PARTY_EVENT.constructItem());
        player.getInventory().setItem(PartyItem.FIGHT_OTHER_PARTY.getSlot(), PartyItem.FIGHT_OTHER_PARTY.constructItem());
        player.getInventory().setItem(PartyItem.PARTY_INFO.getSlot(), PartyItem.PARTY_INFO.constructItem());
        player.getInventory().setItem(PartyItem.PARTY_LEAVE.getSlot(), PartyItem.PARTY_LEAVE.constructItem());
        player.updateInventory();
    }
}
