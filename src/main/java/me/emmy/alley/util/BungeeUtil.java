package me.emmy.alley.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lombok.experimental.UtilityClass;
import me.emmy.alley.Alley;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.chat.JSONMessage;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Emmy
 * @project Alley
 * @date 24/03/2024 - 12:17
 */
@UtilityClass
public class BungeeUtil {
    /**
     * Send a player to another server
     *
     * @param player The player to send
     * @param server The server to send the player to
     */
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

    /**
     * Send a global clickable message to all players
     *
     * @param player The player to send the message to
     * @param message The message to send
     * @param command The command to run when the message is clicked
     */
    public void sendGlobalClickableMessage(Player player, String message, String command) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("MessageRaw");
        dataOutput.writeUTF("ALL");
        dataOutput.writeUTF(JSONMessage.create(CC.translate(message))
                .tooltip(CC.translate(Alley.getInstance().getConfig().getString("announce.hover")))
                .runCommand("/" + command).toString());

        if (player != null) {
            player.sendPluginMessage(Alley.getInstance(), "BungeeCord", dataOutput.toByteArray());
        }
    }
}