package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.UUID;

/**
 * @author Emmy
 * @project Practice
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
     * Reset the players effects, inventory, health, food, etc.
     *
     * @param player the player to reset
     */
    public void reset(Player player) {
        //From Praxi
        player.setHealth(20.0D);
        player.setSaturation(20.0F);
        player.setFallDistance(0.0F);
        player.setFoodLevel(20);
        player.setFireTicks(0);
        player.setMaximumNoDamageTicks(20);
        player.setExp(0.0F);
        player.setLevel(0);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().setArmorContents(new ItemStack[4]);
        player.getInventory().setContents(new ItemStack[36]);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));
        //player.getInventory().setHeldItemSlot(0);
        player.updateInventory();
    }
}
