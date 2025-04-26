package dev.revere.alley.profile.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.EnumHotbarType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;


public class ProfileListener implements Listener {
    private final ProfileService profileService;

    /**
     * Constructor for the ProfileListener class.
     *
     * @param profileService The profile repository.
     */
    public ProfileListener(ProfileService profileService) {
        this.profileService = profileService;
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

        this.profileService.addProfile(profile);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        if (!Alley.getInstance().isLoaded()) {
            event.getPlayer().kickPlayer(CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setFfaMatch(null);
        profile.setOnline(true);
        profile.setMatch(null);

        profile.getProfileData().getProfilePlayTimeData().setLastLogin(System.currentTimeMillis());

        profile.getProfileData().determineLevel();

        Alley.getInstance().getSpawnService().teleportToSpawn(player);
        Alley.getInstance().getHotbarService().applyHotbarItems(player, EnumHotbarType.LOBBY);

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        event.setJoinMessage(null);

        profile.getProfileData().getProfileSettingData().setTimeBasedOnProfileSetting(player);

        profile.save();

        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();
        if (config.getBoolean("welcome-message.enabled")) {
            for (String message : config.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", Alley.getInstance().getPluginConstant().getVersion())
                        .replace("{author}", Alley.getInstance().getDescription().getAuthors().toString().replace("[", "").replace("]", ""))
                );
            }
        }

        if (player.hasPermission("alley.donator.fly")) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
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
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        event.setQuitMessage(null);

        profile.updatePlayTime();
        profile.setOnline(false);
        profile.save();
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.LOBBY) {
            if (player.getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);
        }

        Block block = event.getClickedBlock();
        if (block != null && block.getState() instanceof InventoryHolder) {
            if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER ||
                    block.getType() == Material.FURNACE || block.getType() == Material.BREWING_STAND) {
                event.setCancelled(true);
            }
        }
    }
}
