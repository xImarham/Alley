package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

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
}