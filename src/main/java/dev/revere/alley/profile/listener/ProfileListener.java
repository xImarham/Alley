package dev.revere.alley.profile.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileRepository;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
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
    
    private final ProfileRepository profileRepository;

    /**
     * Constructor for the ProfileListener class.
     *
     * @param profileRepository The profile repository.
     */
    public ProfileListener(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        if (!Alley.getInstance().isLoaded()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.load();

        this.profileRepository.addProfile(profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!Alley.getInstance().isLoaded()) {
            event.getPlayer().kickPlayer(CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        Player player = event.getPlayer();
        Profile profile = this.profileRepository.getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setFfaMatch(null);
        profile.setOnline(true);
        profile.setMatch(null);

        PlayerUtil.reset(player, false);
        Alley.getInstance().getSpawnService().teleportToSpawn(player);
        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.updateInventory();

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        event.setJoinMessage(null);

        profile.getProfileData().getProfileSettingData().setTimeBasedOnProfileSetting(player);

        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();
        if (config.getBoolean("welcome-message.enabled")) {
            for (String message : config.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", Alley.getInstance().getDescription().getVersion())
                        .replace("{author}", Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", ""))
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
        Profile profile = this.profileRepository.getProfile(player.getUniqueId());
        event.setQuitMessage(null);
        profile.setOnline(false);
        profile.save();
    }
}
