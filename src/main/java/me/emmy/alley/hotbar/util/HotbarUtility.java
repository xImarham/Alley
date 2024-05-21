package me.emmy.alley.hotbar.util;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.LobbyItem;
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
}
