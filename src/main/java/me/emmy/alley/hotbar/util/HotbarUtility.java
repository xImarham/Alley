package me.emmy.alley.hotbar.util;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.HotbarItem;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.PlayerUtil;
import org.bukkit.entity.Player;

public class HotbarUtility {

    /**
     * Applies the spawn items to the player's inventory.
     *
     * @param player The player to apply the spawn items to.
     */
    public void applySpawnItems(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        PlayerUtil.reset(player);

        if (profile.getState() == EnumProfileState.LOBBY) {
            player.getInventory().setItem(HotbarItem.UNRANKED_QUEUE.getSlot(), HotbarItem.UNRANKED_QUEUE.constructItem());
            player.getInventory().setItem(HotbarItem.RANKED_QUEUE.getSlot(), HotbarItem.RANKED_QUEUE.constructItem());
            player.getInventory().setItem(HotbarItem.LEADERBOARD.getSlot(), HotbarItem.LEADERBOARD.constructItem());
            player.getInventory().setItem(HotbarItem.EVENTS.getSlot(), HotbarItem.EVENTS.constructItem());
            player.getInventory().setItem(HotbarItem.SETTINGS.getSlot(), HotbarItem.SETTINGS.constructItem());
        } else if (profile.getState() == EnumProfileState.WAITING) {
            player.getInventory().setItem(HotbarItem.LEAVE_QUEUE.getSlot(), HotbarItem.LEAVE_QUEUE.constructItem());
        }

        player.updateInventory();
    }
}
