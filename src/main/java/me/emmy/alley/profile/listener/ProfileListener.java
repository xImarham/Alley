package me.emmy.alley.profile.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.hotbar.enums.HotbarType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.ProfileRepository;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.PlayerUtil;
import me.emmy.alley.util.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class ProfileListener implements Listener {
    
    private final ProfileRepository profileRepository = Alley.getInstance().getProfileRepository();

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }
        
        if (profileRepository == null) {
            return;
        }

        if (profileRepository.getProfile(event.getPlayer().getUniqueId()) != null) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.load();

        profileRepository.getProfiles().put(event.getPlayer().getUniqueId(), profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Profile profile = profileRepository.getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setFfaMatch(null);
        profile.setOnline(true);
        profile.setMatch(null);

        PlayerUtil.reset(player);
        Alley.getInstance().getSpawnHandler().teleportToSpawn(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        event.setJoinMessage(null);

        FileConfiguration config = ConfigHandler.getInstance().getMessagesConfig();
        if (config.getBoolean("welcome-message.enabled")) {
            for (String message : config.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", Alley.getInstance().getDescription().getVersion())
                        .replace("{author}", Alley.getInstance().getDescription().getAuthors().get(0))
                );
            }
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.LOBBY
                || profile.getState() == EnumProfileState.SPECTATING
                || profile.getState() == EnumProfileState.EDITING
                || profile.getState() == EnumProfileState.WAITING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = profileRepository.getProfile(player.getUniqueId());
        profile.setOnline(false);
        profile.save();
    }
}
