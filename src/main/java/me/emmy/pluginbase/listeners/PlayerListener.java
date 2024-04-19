package me.emmy.pluginbase.listeners;

import me.emmy.pluginbase.PluginBase;
import me.emmy.pluginbase.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

/**
 * Created by Emmy
 * Project: PluginBase
 * Date: 19/04/2024 - 18:20
 */

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        UUID playerUUID = joinedPlayer.getUniqueId();

        joinedPlayer.setFlySpeed(1 * 0.1F);
        joinedPlayer.setWalkSpeed(2 * 0.1F);

        event.setJoinMessage(null);
        Location spawnLocation = PluginBase.getInstance().getSpawnLocation();
        if (spawnLocation != null) {
            event.getPlayer().teleport(spawnLocation);
        } else {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&4&l(!) SPAWN LOCATION IS NULL (!)"));
        }
    }
}