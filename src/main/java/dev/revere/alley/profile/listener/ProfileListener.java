package dev.revere.alley.profile.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.hotbar.enums.EnumHotbarType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.enums.EnumProfileState;
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
    protected final Alley plugin;
    protected final ProfileService profileService;

    /**
     * Constructor for the ProfileListener class.
     *
     * @param plugin The Alley instance
     */
    public ProfileListener(Alley plugin) {
        this.plugin = plugin;
        this.profileService = plugin.getProfileService();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event) {
        if (!this.plugin.isLoaded()) {
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
    private void onJoin(PlayerJoinEvent event) {
        if (!this.plugin.isLoaded()) {
            event.getPlayer().kickPlayer(CC.translate("&cThe server is still loading, please try again in a few seconds."));
            return;
        }

        event.setJoinMessage(null);

        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        this.handlePlayerJoin(profile, player);
        this.sendJoinMessage(player);
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
    private void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.profileService.getProfile(player.getUniqueId());

        event.setQuitMessage(null);

        profile.updatePlayTime();
        profile.setOnline(false);
        profile.save();
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
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
        profile.setState(EnumProfileState.LOBBY);
        profile.setName(player.getName());
        profile.setOnline(true);
        profile.setMatch(null);
        profile.setParty(null);
        profile.setFfaMatch(null);

        profile.setNameColor(this.plugin.getCoreAdapter().getCore().getPlayerColor(player));
        this.plugin.getLogger().info("[NametagDebug] SETTING " + player.getName() + "'s color in profile to: " + profile.getNameColor().name());
        profile.getProfileData().getSettingData().setTimeBasedOnProfileSetting(player);
        profile.getProfileData().getPlayTimeData().setLastLogin(System.currentTimeMillis());
        profile.getProfileData().determineLevel();

        player.setFlySpeed(1 * 0.1F);
        player.setWalkSpeed(2 * 0.1F);
        player.getInventory().setHeldItemSlot(0);

        PlayerUtil.reset(player, false);

        this.plugin.getSpawnService().teleportToSpawn(player);
        this.plugin.getHotbarService().applyHotbarItems(player);
        this.plugin.getVisibilityService().updateVisibility(player);

        player.updateInventory();
    }

    /**
     * Sends a welcome message to the player when they join the server.
     * The message is configured in the messages.yml file.
     *
     * @param player The player who joined.
     */
    private void sendJoinMessage(Player player) {
        FileConfiguration config = this.plugin.getConfigService().getMessagesConfig();
        if (config.getBoolean("welcome-message.enabled")) {
            for (String message : config.getStringList("welcome-message.message")) {
                player.sendMessage(CC.translate(message)
                        .replace("{player}", player.getName())
                        .replace("{version}", this.plugin.getPluginConstant().getVersion())
                        .replace("{author}", this.plugin.getDescription().getAuthors().toString().replace("[", "").replace("]", ""))
                );
            }
        }
    }
}