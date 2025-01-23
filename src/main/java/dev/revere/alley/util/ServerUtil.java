package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 13:48
 */
@UtilityClass
public class ServerUtil {
    /**
     * Disconnect all players from the server.
     */
    public void disconnectPlayers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c[&4Alley&c] &cKicked all players due to a server restart."));
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cThe server is restarting.")));
    }

    /**
     * Set the world difficulty to hard and remove all dropped items.
     */
    public void setupWorld() {
        for (World world : Alley.getInstance().getServer().getWorlds()) {
            world.setDifficulty(Difficulty.HARD);
            world.setTime(6000);
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doWeatherCycle", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doMobLoot", "false");
            world.getEntities().forEach(entity -> {
                if (entity.getType() == EntityType.DROPPED_ITEM) {
                    entity.remove();
                }
            });
        }
    }

    /**
     * Clear all entities of a specific type from the server.
     *
     * @param entityType the type of entity to clear
     */
    public void clearEntities(EntityType entityType) {
        for (World world : Alley.getInstance().getServer().getWorlds()) {
            world.getEntities().forEach(entity -> {
                if (entity.getType() == entityType) {
                    entity.remove();
                }
            });
        }
    }

    /**
     * Clear all entities from the server.
     */
    public void clearAllEntities() {
        for (World world : Alley.getInstance().getServer().getWorlds()) {
            world.getEntities().forEach(Entity::remove);
        }
    }

    /**
     * Get the exact bukkit version of the server.
     *
     * @param plugin the plugin instance.
     * @return the exact bukkit version
     */
    public String getBukkitVersionExact(JavaPlugin plugin) {
        String version = plugin.getServer().getVersion();
        version = version.split("MC: ")[1];
        version = version.split("\\)")[0];
        return version;
    }
}