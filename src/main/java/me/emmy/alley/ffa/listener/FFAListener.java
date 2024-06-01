package me.emmy.alley.ffa.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.cooldown.Cooldown;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.ffa.enums.EnumFFAState;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.PlayerUtil;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 25/05/2024 - 14:24
 */

public class FFAListener implements Listener {

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setDeathMessage(null);
            event.getDrops().clear();
            Player killer = PlayerUtil.getLastAttacker(player);
            profile.getFfaMatch().handleDeath(player, killer);
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

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA && profile.getFfaMatch().getState() == EnumFFAState.SPAWN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = player.getItemInHand();

        if (profile.getState() == EnumProfileState.FFA) {
            switch (item.getType()) {
                case ENDER_PEARL:
                    if (profile.getFfaMatch().getState() == EnumFFAState.SPAWN) {
                        event.setCancelled(true);
                        player.updateInventory();
                        player.sendMessage(CC.translate("&cYou cannot use ender pearls at spawn."));
                        return;
                    }

                    Alley alley = Alley.getInstance();
                    CooldownRepository cooldownRepository = alley.getCooldownRepository();

                    Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), "ENDERPEARL"));
                    if (optionalCooldown.isPresent()) {
                        Cooldown cooldown = optionalCooldown.get();
                        if (cooldown.isActive()) {
                            event.setCancelled(true);
                            player.updateInventory();
                            player.sendMessage(CC.translate("&cYou must wait " + cooldown.remainingTime() + " seconds before using another ender pearl."));
                            return;
                        }
                        cooldown.resetCooldown();
                    } else {
                        Cooldown cooldown = new Cooldown(15 * 1000L, () -> player.sendMessage(CC.translate("&aYou can now use pearls again!")));
                        cooldownRepository.addCooldown(player.getUniqueId(), "ENDERPEARL", cooldown);
                    }
                    break;
            }
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.FFA && profile.getFfaMatch() != null) {
            Arena arena = profile.getFfaMatch().getArena();
            Location corner1 = arena.getMinimum();
            Location corner2 = arena.getMaximum();

            //TODO: send one single message whenever the player enters or leaves the spawn

            if (isWithinBounds(event, arena, corner1, corner2)) {
                profile.getFfaMatch().setState(EnumFFAState.SPAWN);
            } else {
                profile.getFfaMatch().setState(EnumFFAState.FIGHTING);
            }
        }
    }


    /**
     * Checks if the player is within the bounds of the arena.
     *
     * @param event The PlayerMoveEvent
     * @param arena The arena the player is in
     * @param corner1 The first corner of the arena
     * @return True if the player is within the bounds of the arena, false otherwise
     */
    private boolean isWithinBounds(PlayerMoveEvent event, Arena arena, Location corner1, Location corner2) {
        double minX = Math.min(corner1.getX(), corner2.getX());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());
        Location to = event.getTo();

        return to.getX() >= minX && to.getX() <= maxX && to.getY() >= minY && to.getY() <= maxY && to.getZ() >= minZ && to.getZ() <= maxZ;
    }
}
