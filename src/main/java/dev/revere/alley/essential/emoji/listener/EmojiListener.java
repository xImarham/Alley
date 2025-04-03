package dev.revere.alley.essential.emoji.listener;

import dev.revere.alley.Alley;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 09:34
 */
public class EmojiListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the EmojiListener class.
     *
     * @param plugin The Alley plugin instance.
     */
    public EmojiListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (!player.hasPermission("alley.donator.chat.symbol")) {
            return;
        }

        for (Map.Entry<String, String> entry : this.plugin.getEmojiRepository().getEmojis().entrySet()) {
            if (message.contains(entry.getKey())) {
                message = message.replace(entry.getKey(), entry.getValue());
            }
        }

        event.setMessage(message);
    }
}