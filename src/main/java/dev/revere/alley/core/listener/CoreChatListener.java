package dev.revere.alley.core.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.core.ICore;
import dev.revere.alley.profile.Profile;
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

        if (this.plugin.getFilterService().isProfanity(event.getMessage())) {
            this.plugin.getFilterService().notifyStaff(event.getMessage(), player);
        }

        String format = core.getChatFormat(player, event.getMessage(), CC.translate("&7: &f"));
        String censoredFormat = core.getChatFormat(player, this.plugin.getFilterService().censorWords(event.getMessage()), CC.translate("&7: &f"));

        Bukkit.getConsoleSender().sendMessage(format);

        for (Player recipient : event.getRecipients()) {
            Profile profile = this.plugin.getProfileService().getProfile(recipient.getUniqueId());
            if (profile.getProfileData().getSettingData().isProfanityFilterEnabled()) {
                if (!event.isCancelled()) {
                    recipient.sendMessage(censoredFormat);
                }
            } else {
                if (!event.isCancelled()) {
                    recipient.sendMessage(format);
                }
            }
        }

        event.setCancelled(true);
    }
}
