package me.emmy.alley.hotbar.util;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.LobbyItem;
import me.emmy.alley.hotbar.PartyItem;
import me.emmy.alley.hotbar.QueueItem;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.queue.Queue;
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

        player.getInventory().setItem(LobbyItem.UNRANKED_QUEUE.getSlot(), LobbyItem.UNRANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.RANKED_QUEUE.getSlot(), LobbyItem.RANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.KIT_EDITOR.getSlot(), LobbyItem.KIT_EDITOR.constructItem());
        player.getInventory().setItem(LobbyItem.PARTY.getSlot(), LobbyItem.PARTY.constructItem());
        player.getInventory().setItem(LobbyItem.LEADERBOARD.getSlot(), LobbyItem.LEADERBOARD.constructItem());
        player.getInventory().setItem(LobbyItem.EVENTS.getSlot(), LobbyItem.EVENTS.constructItem());
        player.getInventory().setItem(LobbyItem.SETTINGS.getSlot(), LobbyItem.SETTINGS.constructItem());

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
