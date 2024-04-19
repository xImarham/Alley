package me.emmy.pluginbase.utils;

import me.emmy.pluginbase.PluginBase;
import me.emmy.pluginbase.utils.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by Emmy
 * Project: FlowerCore
 * Date: 24/03/2024 - 12:17
 */

public class BungeeUtil {

    public static void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
        }
        player.sendMessage(CC.translate(PluginBase.getInstance().getConfig().getString("join.joining").replace("%server%", server)));
        player.sendPluginMessage(PluginBase.getInstance(), "BungeeCord", b.toByteArray());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage(CC.translate(PluginBase.getInstance().getConfig().getString("join.failed").replace("%server%", server)));
            }
        };
        task.runTaskLater(PluginBase.getInstance(), 20);
    }
}