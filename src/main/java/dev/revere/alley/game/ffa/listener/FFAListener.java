package dev.revere.alley.game.ffa.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.combat.ICombatService;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.ICooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingNoHungerImpl;
import dev.revere.alley.game.ffa.cuboid.IFFASpawnService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.InventoryUtil;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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
    /**
     * Accesses the profile of a player.
     *
     * @param player The player
     * @return The profile
     */
    private Profile accessProfile(Player player) {
        return Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        if (ListenerUtil.isSword(event.getItemDrop().getItemStack().getType())) {
            event.setCancelled(true);
            player.sendMessage(CC.translate("&cYou cannot drop your sword during this match."));
            return;
        }

        ListenerUtil.clearDroppedItemsOnRegularItemDrop(event.getItemDrop());
    }

    @EventHandler
    private void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) {
            return;
        }

        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION) {
            Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                player.updateInventory();
            }, 1L);
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;
        event.setDeathMessage(null);

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        Player killer = Alley.getInstance().getService(ICombatService.class).getLastAttacker(player);

        Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);
        Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> profile.getFfaMatch().handleDeath(player, killer), 1L);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
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

        ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
        if (combatService.isPlayerInCombat(player.getUniqueId())) {
            profile.getFfaMatch().handleCombatLog(player, combatService.getLastAttacker(player));
        }

        profile.getFfaMatch().leave(player);
    }

    /**
     * Handles the EntityDamageByEntityEvent.
     * The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();

        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) return;

        ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
        combatService.setLastAttacker(player, attacker);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player attacker;

        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                attacker = (Player) ((Projectile) event.getDamager()).getShooter();
            } else {
                return;
            }
        } else {
            return;
        }

        if (!(event.getEntity() instanceof Player)) return;
        Player victim = (Player) event.getEntity();

        Profile profile = this.accessProfile(victim);
        if (profile.getState() != EnumProfileState.FFA) {
            return;
        }

        IFFASpawnService ffaSpawnService = Alley.getInstance().getService(IFFASpawnService.class);
        if ((ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (!ffaSpawnService.getCuboid().isIn(victim) && ffaSpawnService.getCuboid().isIn(attacker)) || (ffaSpawnService.getCuboid().isIn(victim) && !ffaSpawnService.getCuboid().isIn(attacker))) {
            ICombatService combatService = Alley.getInstance().getService(ICombatService.class);
            if (combatService.isPlayerInCombat(victim.getUniqueId()) && combatService.isPlayerInCombat(attacker.getUniqueId())) {
                return;
            }

            event.setCancelled(true);
        }
    }

    /**
     * Handles the BlockPlaceEvent.
     * The event is cancelled if the player is in the FFA state and tries to place a block.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) {
            return;
        }
        event.setCancelled(true);
    }

    /**
     * Handles the BlockBreakEvent.
     * The event is cancelled if the player is in the FFA state and tries to break a block.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.accessProfile(player);
        if (profile.getState() != EnumProfileState.FFA) {
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    private void onPearlLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity() instanceof EnderPearl)) return;
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player player = (Player) event.getEntity().getShooter();
        Profile profile = this.accessProfile(player);

        if (profile.getState() != EnumProfileState.FFA) return;

        if (Alley.getInstance().getService(IFFASpawnService.class).getCuboid().isIn(player)) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
            player.updateInventory();
            player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
            return;
        }

        ICooldownRepository cooldownRepository = Alley.getInstance().getService(ICooldownRepository.class);
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), EnumCooldownType.ENDER_PEARL));

        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            event.setCancelled(true);
            InventoryUtil.giveItem(player, Material.ENDER_PEARL, 1);
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

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = this.accessProfile(player);
            if (profile.getState() != EnumProfileState.FFA) return;

            if (profile.getFfaMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }
}