package me.emmy.alley.utils;

import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 13:48
 */
public class ServerUtil {
    public static void disconnectPlayers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c[&4Alley&c] &cKicked all players due to a server restart."));
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cThe server is restarting.")));
    }
}
