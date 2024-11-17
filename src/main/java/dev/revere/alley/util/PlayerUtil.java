package dev.revere.alley.util;

import dev.revere.alley.Alley;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 29/04/2024 - 18:53
 */
@UtilityClass
public class PlayerUtil {
    /**
     * Set the last attacker of a player
     *
     * @param victim   the player that was attacked
     * @param attacker the player that attacked
     */
    public void setLastAttacker(Player victim, Player attacker) {
        victim.setMetadata("lastAttacker", new FixedMetadataValue(Alley.getInstance(), attacker.getUniqueId()));
    }

    /**
     * Get the last attacker of a player
     *
     * @param victim the player that was attacked
     * @return the last attacker of the player
     */
    public Player getLastAttacker(Player victim) {
        if (victim.hasMetadata("lastAttacker")) {
            return Bukkit.getPlayer((UUID) victim.getMetadata("lastAttacker").get(0).value());
        } else {
            return null;
        }
    }

    /**
     * Reset a player's stats
     *
     * @param player         the player to reset
     * @param closeInventory whether to close the player's inventory
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