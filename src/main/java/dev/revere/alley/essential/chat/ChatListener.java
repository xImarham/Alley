package dev.revere.alley.essential.chat;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 12/04/2025
 */
public class ChatListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the ChatListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ChatListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onChat(AsyncPlayerChatEvent event) {
        String playerName = event.getPlayer().getName();
        String originalMessage = event.getMessage();

        /*DateFormatter currentTime = new DateFormatter(EnumDateFormat.TIME, System.currentTimeMillis());
        String formattedTime = currentTime.getDateFormat().format(currentTime.getDate());*/

        String cleanedMessage = ChatColor.stripColor(CC.translate(originalMessage));
        String discordMessage = /*"[" + formattedTime + "] */"**" + playerName + "**: " + cleanedMessage;

        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, () ->
                this.plugin.getDiscordBridge().getWebhookService().sendMessage(discordMessage)
        );
    }
}