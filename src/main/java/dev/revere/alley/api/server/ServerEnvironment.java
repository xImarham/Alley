package dev.revere.alley.api.server;

import dev.revere.alley.util.chat.CC;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This class is made for preparing the server environment.
 * Mainly during startup to pre-setup the server with specific settings.
 *
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class ServerEnvironment {
    protected final JavaPlugin plugin;

    private final boolean doDaylightCycle;
    private final boolean doWeatherCycle;

    private final boolean doMobSpawning;
    private final boolean doMobLoot;

    private final boolean removeDroppedItemsOnEnable;

    /**
     * Constructor for the ServerEnvironment class.
     *
     * @param plugin                     the plugin instance.
     * @param doDaylightCycle            whether to enable daylight cycle.
     * @param doWeatherCycle             whether to enable weather cycle.
     * @param doMobSpawning              whether to enable mob spawning.
     * @param doMobLoot                  whether to enable mob loot.
     * @param removeDroppedItemsOnEnable whether to remove dropped items on enable.
     */
    public ServerEnvironment(JavaPlugin plugin, boolean doDaylightCycle, boolean doWeatherCycle, boolean doMobSpawning, boolean doMobLoot, boolean removeDroppedItemsOnEnable) {
        this.plugin = plugin;

        this.doDaylightCycle = doDaylightCycle;
        this.doWeatherCycle = doWeatherCycle;

        this.doMobSpawning = doMobSpawning;
        this.doMobLoot = doMobLoot;

        this.removeDroppedItemsOnEnable = removeDroppedItemsOnEnable;
    }

    /**
     * Disconnect all players from the server.
     */
    public void disconnectPlayers() {
        this.plugin.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cThe server is restarting.")));
    }

    /**
     * Set the world difficulty to hard and remove all dropped items.
     */
    public void setupWorld() {
        for (World world : this.plugin.getServer().getWorlds()) {
            world.setDifficulty(Difficulty.HARD);
            world.setTime(6000);
            world.setGameRuleValue("doDaylightCycle", String.valueOf(this.doDaylightCycle));
            world.setGameRuleValue("doWeatherCycle", String.valueOf(this.doWeatherCycle));
            world.setGameRuleValue("doMobSpawning", String.valueOf(this.doMobSpawning));
            world.setGameRuleValue("doMobLoot", String.valueOf(this.doMobLoot));
            if (this.removeDroppedItemsOnEnable) {
                world.getEntities().forEach(entity -> {
                    if (entity.getType() == EntityType.DROPPED_ITEM) {
                        entity.remove();
                    }
                });
            }
        }
    }

    /**
     * Clear all entities of a specific type from the server.
     *
     * @param entityType the type of entity to clear
     */
    public void clearEntities(EntityType entityType) {
        for (World world : this.plugin.getServer().getWorlds()) {
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
        for (World world : this.plugin.getServer().getWorlds()) {
            world.getEntities().forEach(Entity::remove);
        }
    }
}