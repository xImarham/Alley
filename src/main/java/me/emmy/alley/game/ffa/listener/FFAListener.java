package me.emmy.alley.game.ffa.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.cooldown.Cooldown;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.game.ffa.combat.CombatTagEvent;
import me.emmy.alley.game.ffa.safezone.FFASpawnHandler;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
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
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:24
 */
public class FFAListener implements Listener {

    /**
     * Handles the CombatTagEvent.
     *
     * @param event The CombatTagEvent
     */
    @EventHandler
    public void onCombatTag(CombatTagEvent event) {
        Player player = event.getPlayer();
        Player attacker = event.getAttacker();

        // call this at the onEntityDamageByEntity event

        Profile playerProfile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        Profile attackerProfile = Alley.getInstance().getProfileRepository().getProfile(attacker.getUniqueId());
        if (playerProfile.getState() == EnumProfileState.FFA && attackerProfile.getState() == EnumProfileState.FFA) {
            player.sendMessage(CC.translate("&cYou are now in combat with &4" + attacker.getName() + "&c."));
            attacker.sendMessage(CC.translate("&cYou are now in combat with &4" + player.getName() + "&c."));
        }
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.getItemDrop().remove();
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.FFA) {
            event.setDeathMessage(null);

            List<Item> droppedItems = new ArrayList<>();
            for (ItemStack drop : event.getDrops()) {
                if (drop != null && drop.getType() != Material.AIR) {
                    droppedItems.add(player.getWorld().dropItemNaturally(player.getLocation(), drop));
                }
            }
            event.getDrops().clear();

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Item item : droppedItems) {
                        if (item != null && item.isValid()) {
                            item.remove();
                        }
                    }
                }
            }.runTaskLater(Alley.getInstance(), 100L);

            Player killer = PlayerUtil.getLastAttacker(player);
            if (killer != null) {
                player.sendMessage(CC.translate("&cYou have been killed by &4" + killer.getName() + "&c."));
                killer.sendMessage(CC.translate("&aYou have killed &2" + player.getName() + "&a."));
            }

            Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);

            Bukkit.getScheduler().runTaskLater(Alley.getInstance(), () -> {
                profile.getFfaMatch().handleDeath(player, killer);
            }, 1L);
        }
    }


    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getFfaMatch().leave(player);
        }
    }

    @EventHandler
    private void onKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            profile.getFfaMatch().leave(player);
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == EnumProfileState.FFA) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                PlayerUtil.setLastAttacker(player, damager);
            }
        }
    }

    /**
     * Handles the EntityDamageByEntityEvent. The event is cancelled if the player is in the FFA state and tries to damage another player.
     *
     * @param event The EntityDamageByEntityEvent
     */
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        FFASpawnHandler ffaSpawnHandler = Alley.getInstance().getFfaSpawnHandler();
        if (ffaSpawnHandler.getCuboid().isIn((victim)) && ffaSpawnHandler.getCuboid().isIn((attacker)) ||
                !ffaSpawnHandler.getCuboid().isIn(victim) && ffaSpawnHandler.getCuboid().isIn(attacker) ||
                ffaSpawnHandler.getCuboid().isIn(victim) && !ffaSpawnHandler.getCuboid().isIn(attacker)) {

            event.setCancelled(true);
        }

        /*CombatManager combatManager = Alley.getInstance().getCombatManager();

        Bukkit.getPluginManager().callEvent(new CombatTagEvent(victim, attacker));
        combatManager.setCombatTime(victim, 16);
        combatManager.setCombatTime(attacker, 16);
        combatManager.setCombatSet(victim, true);
        combatManager.setCombatSet(attacker, true);*/
    }

    /**
     * Handles the BlockPlaceEvent. The event is cancelled if the player is in the FFA state and tries to place a block.
     *
     * @param event The BlockPlaceEvent
     */
    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    /**
     * Handles the BlockBreakEvent. The event is cancelled if the player is in the FFA state and tries to break a block.
     *
     * @param event The BlockBreakEvent
     */
    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
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
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (profile.getState() == EnumProfileState.FFA && item != null && item.getType() == Material.ENDER_PEARL) {
            if (event.getAction().name().contains("RIGHT_CLICK")) {
                if (Alley.getInstance().getFfaSpawnHandler().getCuboid().isIn(player)) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
                    return;
                }

                CooldownRepository cooldownRepository = Alley.getInstance().getCooldownRepository();
                Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));

                if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
                    event.setCancelled(true);
                    player.updateInventory();
                    player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTime() + " seconds before using another ender pearl."));
                    return;
                }

                Cooldown cooldown = optionalCooldown.orElseGet(() -> {
                    Cooldown newCooldown = new Cooldown(15 * 1000L, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
                    cooldownRepository.addCooldown(player.getUniqueId(), "ENDERPEARL", newCooldown);
                    return newCooldown;
                });

                cooldown.resetCooldown();
            }
        }
    }

}
