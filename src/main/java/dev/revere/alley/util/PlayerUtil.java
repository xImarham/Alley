package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:53
 */
@UtilityClass
public class PlayerUtil {
    /**
     * Reset a player's state to default values.
     *
     * @param player         the player to reset.
     * @param closeInventory whether to close the player's inventory after resetting.
     */
    public void reset(Player player, boolean closeInventory) {
        player.setHealth(20.0D);
        player.setSaturation(20.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);

        player.setAllowFlight(false);
        player.setFlying(false);

        player.setExp(0.0F);
        player.setLevel(0);

        player.setGameMode(GameMode.SURVIVAL);

        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);

        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        player.updateInventory();

        if (closeInventory) {
            player.closeInventory();
        }
    }

    /**
     * Get an offline player by their name
     *
     * @param name the name of the player
     * @return the offline player
     */
    public OfflinePlayer getOfflinePlayerByName(String name) {
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (offlinePlayer.getName().equalsIgnoreCase(name)) {
                return offlinePlayer;
            }
        }
        return null;
    }
}