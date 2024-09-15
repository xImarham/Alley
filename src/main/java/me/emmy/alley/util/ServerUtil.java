package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;

/**
 * @author Emmy
 * @project Alley
 * @date 01/06/2024 - 13:48
 */
@UtilityClass
public class ServerUtil {
    public void disconnectPlayers() {
        Bukkit.getConsoleSender().sendMessage(CC.translate("&c[&4Alley&c] &cKicked all players due to a server restart."));
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer(CC.translate("&cThe server is restarting.")));
    }
}
