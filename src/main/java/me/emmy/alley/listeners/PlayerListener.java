package me.emmy.alley.listeners;

import me.emmy.alley.Alley;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
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
 * Project: Alley
 * Date: 19/04/2024 - 18:20
 */

public class PlayerListener implements Listener {

    private final Alley plugin = Alley.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        UUID playerUUID = joinedPlayer.getUniqueId();

        joinedPlayer.setFlySpeed(1 * 0.1F);
        joinedPlayer.setWalkSpeed(2 * 0.1F);

        event.setJoinMessage(null);

        PlayerUtil.reset(joinedPlayer);
        PlayerUtil.teleportToSpawn(joinedPlayer);
        joinedPlayer.getInventory().setHeldItemSlot(0);
        Alley.getInstance().getItemManager().applySpawnItems(joinedPlayer);

        if (plugin.getConfig("messages.yml").getBoolean("welcome-message.enabled")) {
            for (String message : plugin.getConfig("messages.yml").getStringList("welcome-message.message")) {
                joinedPlayer.sendMessage(CC.translate(message)
                        .replace("{player}", joinedPlayer.getName())
                        .replace("{version}", plugin.getDescription().getVersion())
                        .replace("{author}", plugin.getDescription().getAuthors().get(0))
                );
            }
        }
    }
}