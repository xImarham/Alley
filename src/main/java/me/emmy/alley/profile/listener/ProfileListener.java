package me.emmy.alley.profile.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final Alley plugin = Alley.getInstance();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        if (plugin.getProfileRepository() == null) {
            return;
        }

        if (plugin.getProfileRepository().getProfile(event.getPlayer().getUniqueId()) != null) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.load();

        plugin.getProfileRepository().getProfiles().put(event.getPlayer().getUniqueId(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setFfaGame(null);
        profile.setOnline(true);
        profile.setMatch(null);

        PlayerUtil.reset(player);
        Alley.getInstance().getSpawnHandler().teleportToSpawn(player);
        Alley.getInstance().getHotbarUtility().applySpawnItems(player);

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        event.setJoinMessage(null);


        if (plugin.getConfig("messages.yml").getBoolean("welcome-message.enabled")) {
            for (String message : plugin.getConfig("messages.yml").getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", plugin.getDescription().getVersion())
                        .replace("{author}", plugin.getDescription().getAuthors().get(0))
                );
            }
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = plugin.getProfileRepository().getProfile(player.getUniqueId());
        if (profile == null) {
            CC.broadcast(" \n&c&l" + player.getName() + "'s profile is null ??? \n ");
            return;
        }

        profile.setOnline(false);
        profile.save();
        if (profile.getQueueProfile() == null) {
            CC.broadcast(" \n&c&l" + player.getName() + " is not in a queue and therefore couldn't be removed.\n ");
            return;
        }

        CC.broadcast(" \n&c&l" + player.getName() + " has been removed from the queue.\n ");
        profile.getQueueProfile().getQueue().removePlayer(profile.getQueueProfile());
    }
}
