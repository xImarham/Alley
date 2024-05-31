package me.emmy.alley.match.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.cooldown.Cooldown;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.kit.settings.impl.KitSettingBoxingImpl;
import me.emmy.alley.kit.settings.impl.KitSettingBuildImpl;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class MatchListener implements Listener {

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING) event.setCancelled(true);
        if (profile.getState() == EnumProfileState.PLAYING) {
            if (profile.getMatch().getMatchState() != EnumMatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getGamePlayer(player).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                event.setDamage(0);
                player.setHealth(20.0);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
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

        if (attacker != null && event.getEntity() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Profile damagedprofile = Alley.getInstance().getProfileRepository().getProfile(damaged.getUniqueId());
            Profile attackerProfile = Alley.getInstance().getProfileRepository().getProfile(attacker.getUniqueId());

            if (damagedprofile.getState() == EnumProfileState.SPECTATING) {
                event.setCancelled(true);
                return;
            }

            if (damagedprofile.getState() == EnumProfileState.PLAYING) {
                AbstractMatch match = attackerProfile.getMatch();
                if (damagedprofile.getMatch().getMatchState() != EnumMatchState.RUNNING) {
                    event.setCancelled(true);
                    return;
                }

                if (match.getGamePlayer(damaged).isDead()) {
                    event.setCancelled(true);
                    return;
                }

                if (match.getGamePlayer(attacker).isDead()) {
                    event.setCancelled(true);
                    return;
                }

                attackerProfile.getMatch().getGamePlayer(attacker).getData().handleAttack();
                damagedprofile.getMatch().getGamePlayer(damaged).getData().resetCombo();

                if (match.getMatchKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                    if (match.getGamePlayer(attacker).getData().getHits() >= 100) {
                        match.handleDeath(damaged);
                    }
                }
            }
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        switch (profile.getState()) {
            case PLAYING:
                event.setCancelled(!profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBuildImpl.class));
                break;
            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        switch (profile.getState()) {
            case PLAYING:
                event.setCancelled(!profile.getMatch().getMatchKit().isSettingEnabled(KitSettingBuildImpl.class));
                break;
            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING || profile.getState() == EnumProfileState.PLAYING) {
            if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
                Arena arena = profile.getMatch().getMatchArena();
                Location corner1 = arena.getMinimum();
                Location corner2 = arena.getMaximum();

                double minX = Math.min(corner1.getX(), corner2.getX());
                double maxX = Math.max(corner1.getX(), corner2.getX());
                double minY = Math.min(corner1.getY(), corner2.getY());
                double maxY = Math.max(corner1.getY(), corner2.getY());
                double minZ = Math.min(corner1.getZ(), corner2.getZ());
                double maxZ = Math.max(corner1.getZ(), corner2.getZ());

                Location to = event.getTo();

                boolean withinBounds = to.getX() >= minX && to.getX() <= maxX && to.getY() >= minY && to.getY() <= maxY && to.getZ() >= minZ && to.getZ() <= maxZ;
                if (!withinBounds) {
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou cannot leave the arena."));
                }
            }
        }
    }

    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING || profile.getState() == EnumProfileState.PLAYING) {
            Arena arena = profile.getMatch().getMatchArena();
            Location corner1 = arena.getMinimum();
            Location corner2 = arena.getMaximum();

            double minX = Math.min(corner1.getX(), corner2.getX());
            double maxX = Math.max(corner1.getX(), corner2.getX());
            double minY = Math.min(corner1.getY(), corner2.getY());
            double maxY = Math.max(corner1.getY(), corner2.getY());
            double minZ = Math.min(corner1.getZ(), corner2.getZ());
            double maxZ = Math.max(corner1.getZ(), corner2.getZ());

            Location to = event.getTo();

            boolean withinBounds = to.getX() >= minX && to.getX() <= maxX && to.getY() >= minY && to.getY() <= maxY && to.getZ() >= minZ && to.getZ() <= maxZ;
            if (!withinBounds) {
                player.teleport(event.getFrom());
                player.sendMessage(CC.translate("&cYou cannot leave the arena."));
            }
        }
    }

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.PLAYING) {
            event.setRespawnLocation(player.getLocation());
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            event.setDeathMessage(null);
            event.getDrops().clear();

            Alley.getInstance().getKillEffectRepository().getByName(profile.getProfileData().getActiveKillEffect()).spawnEffect(player.getLocation());
            Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);
            profile.getMatch().handleDeath(player);
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = player.getItemInHand();

        if (profile.getState() == EnumProfileState.PLAYING) {
            switch (item.getType()) {
                case ENDER_PEARL:
                    if (profile.getMatch().getMatchState() == EnumMatchState.STARTING) {
                        event.setCancelled(true);
                        player.updateInventory();
                        player.sendMessage(CC.translate("&cYou cannot use ender pearls."));
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
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING) event.setCancelled(true);
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            AbstractMatch match = profile.getMatch();

            if (match.getMatchState() == EnumMatchState.STARTING || match.getMatchState() == EnumMatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }

    @EventHandler
    private void onPlayerKick(PlayerKickEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
            AbstractMatch match = profile.getMatch();

            if (match.getMatchState() == EnumMatchState.STARTING || match.getMatchState() == EnumMatchState.RUNNING) {
                match.handleDisconnect(player);
            }
        }
    }
}
