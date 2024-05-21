package me.emmy.alley.hotbar.util;

import me.emmy.alley.hotbar.LobbyItem;
import org.bukkit.entity.Player;

public class HotbarUtility {

    /**
     * Applies the spawn items to the player's inventory.
     *
     * @param player The player to apply the spawn items to.
     */
    public void applySpawnItems(Player player) {
        player.getInventory().setItem(LobbyItem.UNRANKED_QUEUE.getSlot(), LobbyItem.UNRANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.RANKED_QUEUE.getSlot(), LobbyItem.RANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.LEADERBOARD.getSlot(), LobbyItem.LEADERBOARD.constructItem());
        player.getInventory().setItem(LobbyItem.EVENTS.getSlot(), LobbyItem.EVENTS.constructItem());
        player.getInventory().setItem(LobbyItem.SETTINGS.getSlot(), LobbyItem.SETTINGS.constructItem());
    }
}
