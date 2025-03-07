package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.combat.CombatService;
import dev.revere.alley.feature.cooldown.Cooldown;
import dev.revere.alley.feature.cooldown.CooldownRepository;
import dev.revere.alley.feature.cooldown.enums.EnumCooldownType;
import dev.revere.alley.game.ffa.cuboid.FFACuboidServiceImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {
    private final Alley plugin;
    
    /**
     * Constructor for the FFAListener class.
     *
     * @param plugin The Alley instance
     */
    public FFAListener(Alley plugin) {
        this.plugin = plugin;
    }

    /**
     * Accesses the profile of a player.
     *
     * @param player The player
     * @return The profile
     */
    private Profile accessProfile(Player player) {
        return this.plugin.getProfileRepository().getProfile(player.getUniqueId());
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        event.getItemDrop().remove();
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;
        event.setDeathMessage(null);

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        Player killer = Alley.getInstance().getCombatService().getLastAttacker(player);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> player.spigot().respawn(), 1L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> profile.getFfaMatch().handleDeath(player, killer), 1L);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        CombatService combatService = Alley.getInstance().getCombatService();
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        CombatService combatService = Alley.getInstance().getCombatService();
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = this.plugin.getProfileRepository().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() != EnumProfileState.FFA) return;

            Player player = (Player) event.getEntity();
            Player attacker = (Player) event.getDamager();
            Alley.getInstance().getCombatService().setLastAttacker(player, attacker);

            CombatService combatService = Alley.getInstance().getCombatService();
            combatService.setLastAttacker(player, attacker);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Profile profile = this.plugin.getProfileRepository().getProfile(event.getEntity().getUniqueId());
        if (profile.getState() != EnumProfileState.FFA) return;
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        
        FFACuboidServiceImpl ffaCuboidService = this.plugin.getFfaCuboidService();
        if (ffaCuboidService.getCuboid().isIn((victim)) && ffaCuboidService.getCuboid().isIn((attacker)) 
                || !ffaCuboidService.getCuboid().isIn(victim) && ffaCuboidService.getCuboid().isIn(attacker) 
                || ffaCuboidService.getCuboid().isIn(victim) && !ffaCuboidService.getCuboid().isIn(attacker)) {

            CombatService combatService = Alley.getInstance().getCombatService();
            if (combatService.isPlayerInCombat(victim.getUniqueId() ) && combatService.isPlayerInCombat(attacker.getUniqueId())) {
                return;
            }

            event.setCancelled(true);
        }
    }

    /**
     * Handles the BlockPlaceEvent. The event is cancelled if the player is in the FFA state and tries to place a block.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;
        
        event.setCancelled(true);
    }

    /**
     * Handles the BlockBreakEvent. The event is cancelled if the player is in the FFA state and tries to break a block.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the PlayerInteractEvent. The event is cancelled if the player is in the FFA state and tries to interact with an item.
     *
     * @param event The PlayerInteractEvent
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        ItemStack item = event.getItem();

        if (profile.getState() != EnumProfileState.FFA) return;
        if (item == null) return;
        if (item.getType() != Material.ENDER_PEARL) return;

        if (event.getAction().name().contains("RIGHT_CLICK")) {
            if (this.plugin.getFfaCuboidService().getCuboid().isIn(player)) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
                return;
            }

            CooldownRepository cooldownRepository = this.plugin.getCooldownRepository();
            Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL));

            if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using another ender pearl."));
                return;
            }

            Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                Cooldown newCooldown = new Cooldown(EnumCooldownType.ENDER_PEARL, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
                cooldownRepository.addCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL, newCooldown);
                return newCooldown;
            });

            cooldown.resetCooldown();
        }
    }
}