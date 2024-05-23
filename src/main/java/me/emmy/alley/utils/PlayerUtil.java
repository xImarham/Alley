package me.emmy.alley.utils;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Emmy
 * Project: Practice
 * Date: 29/04/2024 - 18:53
 */

public class PlayerUtil {
    public static void reset(Player player) {
        if (!player.hasMetadata("frozen")) {
            player.setWalkSpeed(0.2F);
            player.setFlySpeed(0.1F);
        }

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

    public static void teleportToSpawn(Player player) {
        Location spawnLocation = Alley.getInstance().getSpawnHandler().getSpawnLocation();
        if (spawnLocation != null) {
            player.teleport(spawnLocation);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4&l(!) SPAWN LOCATION IS NULL (!)"));
        }
    }
}
