package me.emmy.pluginbase.utils;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.emmy.pluginbase.PluginBase;
import me.emmy.pluginbase.utils.chat.CC;
import me.emmy.pluginbase.utils.chat.JSONMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: FlowerCore
 * Discord: dsc.gg/emmiesa
 * Credit: FrozedUHCMeetup
 */

public class Utils {

    public static List<Player> getOnlinePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            players.add(online);
        }
        return players;
    }

    public static String formatTime(int timer) {
        int hours = timer / 3600;
        int secondsLeft = timer - hours * 3600;
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;

        String formattedTime = "";

        if (hours > 0) {
            if (hours < 10)
                formattedTime += "0";
            formattedTime += hours + ":";
        }

        if (minutes < 10)
            formattedTime += "0";
        formattedTime += minutes + ":";

        if (seconds < 10)
            formattedTime += "0";
        formattedTime += seconds;

        return formattedTime;
    }

    public static Location tptop(Location loc) {
        return new Location(loc.getWorld(), loc.getX(), loc.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()), loc.getZ(), loc.getYaw(), loc.getPitch());
    }

    public static void sendGlobalClickableMessage(Player player, String message, String command) {
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        dataOutput.writeUTF("MessageRaw");
        dataOutput.writeUTF("ALL");
        dataOutput.writeUTF(JSONMessage.create(CC.translate(message))
                .tooltip(CC.translate(PluginBase.getInstance().getConfig().getString("announce.hover")))
                .runCommand("/" + command).toString());

        if (player != null) {
            player.sendPluginMessage(PluginBase.getInstance(), "BungeeCord", dataOutput.toByteArray());
        }
    }

    public static void broadcastMessage(String message) {
        getOnlinePlayers().forEach(player -> player.sendMessage(CC.translate(message)));
    }
}