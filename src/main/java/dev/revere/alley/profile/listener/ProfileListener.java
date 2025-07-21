package dev.revere.alley.profile.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.IPluginConstant;
import dev.revere.alley.base.hotbar.IHotbarService;
import dev.revere.alley.base.spawn.ISpawnService;
import dev.revere.alley.base.visibility.IVisibilityService;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.feature.music.IMusicService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.adapter.core.ICoreAdapter;
import dev.revere.alley.util.PlayerUtil;
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

/**
 * @author Emmy
 * @project Alley
 * @since 19/04/2024
 */
public class ProfileListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        if (!Alley.getInstance().isEnabled()) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            return;
        }

        Profile profile = new Profile(event.getPlayer().getUniqueId());
        profile.load();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        profileService.getProfile(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onJoin(PlayerJoinEvent event) {
        if (!Alley.getInstance().isEnabled()) {
            event.getPlayer().kickPlayer(CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        this.handlePlayerJoin(profile, player);
        this.sendJoinMessage(player);
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.LOBBY
                || profile.getState() == EnumProfileState.SPECTATING
                || profile.getState() == EnumProfileState.EDITING
                || profile.getState() == EnumProfileState.WAITING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        IMusicService musicService = Alley.getInstance().getService(IMusicService.class);

        Profile profile = profileService.getProfile(player.getUniqueId());

        event.setQuitMessage(null);

        musicService.stopMusic(player);

        profile.updatePlayTime();
        profile.setOnline(false);
        profile.save();
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
        Profile profile = profileService.getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.LOBBY
                || profile.getState() == EnumProfileState.EDITING
                || profile.getState() == EnumProfileState.SPECTATING) {
            if (player.getGameMode() == GameMode.CREATIVE) return;
            event.setCancelled(true);

            Block block = event.getClickedBlock();
            if (block != null && block.getState() instanceof InventoryHolder) {
                if (block.getType() == Material.CHEST || block.getType() == Material.DISPENSER || block.getType() == Material.FURNACE || block.getType() == Material.BREWING_STAND) {
                    event.setCancelled(true);
                }
            }
        }
    }

    /**
     * Handles the player joining the server.
     * This method sets the player's profile state to LOBBY, updates their name
     * and online status, including other profile-related data.
     * Also teleports the player to the spawn and applies the lobby hotbar items.
     *
     * @param profile The profile of the player.
     * @param player  The player who joined.
     */
    private void handlePlayerJoin(Profile profile, Player player) {
        ICoreAdapter coreAdapter = Alley.getInstance().getService(ICoreAdapter.class);
        ISpawnService spawnService = Alley.getInstance().getService(ISpawnService.class);
        IHotbarService hotbarService = Alley.getInstance().getService(IHotbarService.class);
        IVisibilityService visibilityService = Alley.getInstance().getService(IVisibilityService.class);
        IMusicService musicService = Alley.getInstance().getService(IMusicService.class);

        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setOnline(true);
        profile.setMatch(null);
        profile.setParty(null);
        profile.setFfaMatch(null);

        profile.setNameColor(coreAdapter.getCore().getPlayerColor(player));
        profile.getProfileData().getSettingData().setTimeBasedOnProfileSetting(player);
        profile.getProfileData().getPlayTimeData().setLastLogin(System.currentTimeMillis());
        profile.getProfileData().determineLevel();

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        PlayerUtil.reset(player, false, true);

        spawnService.teleportToSpawn(player);
        hotbarService.applyHotbarItems(player);
        visibilityService.updateVisibility(player);
        musicService.startMusic(player);

        player.updateInventory();
    }

    /**
     * Sends a welcome message to the player when they join the server.
     * The message is configured in the messages.yml file.
     *
     * @param player The player who joined.
     */
    private void sendJoinMessage(Player player) {
        IConfigService configService = Alley.getInstance().getService(IConfigService.class);
        IPluginConstant constants = Alley.getInstance().getService(IPluginConstant.class);

        FileConfiguration msgConfig = configService.getMessagesConfig();
        if (msgConfig.getBoolean("welcome-message.enabled")) {
            for (String message : msgConfig.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", constants.getVersion())
                        .replace("{author}", constants.getAuthors().toString().replace("[", "").replace("]", ""))
                );
            }
        }
    }
}