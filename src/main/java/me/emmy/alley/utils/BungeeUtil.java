package me.emmy.alley.utils;

import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Emmy
 * @project FlowerCore
 * @date 24/03/2024 - 12:17
 */
@UtilityClass
public class BungeeUtil {

    public void sendPlayer(Player player, String server) {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF("Connect");
            out.writeUTF(server);
        } catch (IOException e) {
        }
        player.sendMessage(CC.translate("&aJoining &b" + server + "&a..."));
        player.sendPluginMessage(Alley.getInstance(), "BungeeCord", b.toByteArray());
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendMessage("join failed");
            }
        };
        task.runTaskLater(Alley.getInstance(), 20);
    }
}