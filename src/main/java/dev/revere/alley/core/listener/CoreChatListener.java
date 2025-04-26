package dev.revere.alley.core.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.core.ICore;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public class CoreChatListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the CoreChatListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public CoreChatListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        ICore core = this.plugin.getCoreAdapter().getCore();

        Bukkit.getOnlinePlayers().forEach(
            onlinePlayer -> onlinePlayer.sendMessage(core.getChatFormat(player, event.getMessage(), CC.translate("&7: &f")))
        );

        event.setCancelled(true);
    }
}
