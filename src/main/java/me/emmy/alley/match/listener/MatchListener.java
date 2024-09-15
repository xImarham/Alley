package me.emmy.alley.match.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.cooldown.Cooldown;
import me.emmy.alley.cooldown.CooldownRepository;
import me.emmy.alley.kit.settings.impl.*;
import me.emmy.alley.locale.ErrorMessage;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.match.player.GameParticipant;
import me.emmy.alley.match.player.impl.MatchGamePlayerImpl;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.util.PlayerUtil;
import me.emmy.alley.util.location.RayTracerUtil;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class MatchListener implements Listener {

    private final Random random = new Random();

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

            if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingSumoImpl.class)) {
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

            if (attackerProfile.getState() == EnumProfileState.SPECTATING) {
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
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL && event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

            if (profile.getState() == EnumProfileState.PLAYING && profile.getMatch().getMatchState() == EnumMatchState.RUNNING &&
                    profile.getMatch().getMatchKit().isSettingEnabled(KitSettingSpleefImpl.class)) {

                Snowball snowball = (Snowball) event.getEntity();
                Location hitLocation = RayTracerUtil.rayTrace(snowball.getLocation(), snowball.getVelocity().normalize());

                if (hitLocation.getBlock().getType() == Material.SNOW || hitLocation.getBlock().getType() == Material.SNOW_BLOCK) {
                    hitLocation.getBlock().setType(Material.AIR);
                    player.sendMessage(CC.translate(ErrorMessage.DEBUG));
                }
            }
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.SPECTATING) {
            event.setCancelled(true);
            return;
        }

        if (profile.getState() == EnumProfileState.PLAYING &&
                profile.getMatch().getMatchState() == EnumMatchState.RUNNING &&
                profile.getMatch().getMatchKit().isSettingEnabled(KitSettingSpleefImpl.class)) {

            Block block = event.getBlock();
            if (block.getType() == Material.SNOW_BLOCK) {
                event.setCancelled(false);
                event.getBlock().setType(Material.AIR);

                int amount = random.nextInt(100) < 10 ? random.nextInt(3) + 2 : 0;
                if (amount > 0) {
                    ItemStack snowballs = new ItemStack(Material.SNOW_BALL, amount);
                    player.getInventory().addItem(snowballs);
                }
            } else {
                event.setCancelled(true);
            }
        } else {
            Bukkit.getConsoleSender().sendMessage(ErrorMessage.DEBUG);
            if (player.getGameMode() == GameMode.CREATIVE) return;

            event.setCancelled(true);
        }
    }

    /*@EventHandler
    private void onEntityInteract(EntityInteractEvent event) {
        if (event.getEntity() instanceof Player) {
            Player winner = (Player) event.getEntity();
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(winner.getUniqueId());
            if (profile.getState() == EnumProfileState.PLAYING) {
                if (profile.getMatch() == null) {
                    return;
                }

                if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingParkourImpl.class)) {
                    if (event.getBlock().equals(Material.STONE_PLATE)) {
                        for (GameParticipant<MatchGamePlayerImpl> participant : profile.getMatch().getParticipants()) {
                            Player loser = participant.getPlayer().getPlayer();
                                loser.setHealth(0);
                        }
                    }
                }
            }
        }
    }*/

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

    /**
     * handles player move event:
     * checks if player is in a match and if they are playing sumo or spleef
     * and depending on that it checks if they moved away from arena pos1 and tps back
     *
     * @param event The PlayerMoveEvent.
     */
    @EventHandler
    private void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile != null && profile.getMatch() != null && profile.getState() == EnumProfileState.PLAYING && profile.getMatch().getMatchState() == EnumMatchState.RUNNING) {
            if (profile.getMatch().getMatchKit().isSettingEnabled(KitSettingSumoImpl.class) || profile.getMatch().getMatchKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                    player.setHealth(0);
                }
            }
        }

        if (profile != null && profile.getMatch() != null && profile.getState() == EnumProfileState.PLAYING) {
            AbstractMatch match = profile.getMatch();
            if (match.getMatchState() == EnumMatchState.STARTING) {
                if (match.getMatchKit().isSettingEnabled(KitSettingSumoImpl.class) || match.getMatchKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                    List<GameParticipant<MatchGamePlayerImpl>> participants = match.getParticipants();
                    if (participants.size() == 2) {
                        GameParticipant<?> participantA = participants.get(0);
                        GameParticipant<?> participantB = participants.get(1);

                        Player playerA = participantA.getPlayer().getPlayer();
                        Player playerB = participantB.getPlayer().getPlayer();

                        Location playerLocation = player.getLocation();
                        Location locationA = match.getMatchArena().getPos1();
                        Location locationB = match.getMatchArena().getPos2();

                        if (player.equals(playerA)) {
                            if (playerLocation.getBlockX() != locationA.getBlockX() || playerLocation.getBlockZ() != locationA.getBlockZ()) {
                                player.teleport(new Location(locationA.getWorld(), locationA.getX(), playerLocation.getY(), locationA.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
                                CC.broadcast("&4" + playerA.getDisplayName() + " &cmoved | Teleporting back...");
                            }
                        } else if (player.equals(playerB)) {
                            if (playerLocation.getBlockX() != locationB.getBlockX() || playerLocation.getBlockZ() != locationB.getBlockZ()) {
                                player.teleport(new Location(locationB.getWorld(), locationB.getX(), playerLocation.getY(), locationB.getZ(), playerLocation.getYaw(), playerLocation.getPitch()));
                                CC.broadcast("&4" + playerB.getDisplayName() + " &cmoved | Teleporting back...");
                            }
                        }
                    }
                }
            }
        }

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
        Player killer = player.getKiller();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.PLAYING) {
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

            Alley.getInstance().getServer().getScheduler().runTaskLater(Alley.getInstance(), () -> player.spigot().respawn(), 1L);
            profile.getMatch().handleDeath(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == EnumProfileState.SPECTATING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getState() == EnumProfileState.PLAYING) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                PlayerUtil.setLastAttacker(player, damager);
            }
        }
    }

    /**
     * Prevents players from using ender pearls during the starting phase of a match.
     * Prevents players from using ender pearls if they are on cooldown.
     *
     * @param event The PlayerInteractEvent.
     */
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (event.getAction() != Action.PHYSICAL) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.STONE_PLATE) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null || !match.getMatchKit().isSettingEnabled(KitSettingParkourImpl.class)) {
            return;
        }

        Bukkit.broadcastMessage(player.getName() + " stepped on a stone pressure plate and won the match!");
        match.getParticipants().forEach(participant -> {
            Player participantPlayer = participant.getPlayer().getPlayer();
            if (participantPlayer != null && !participantPlayer.getUniqueId().equals(player.getUniqueId())) {
                match.handleDeath(participantPlayer);
            }
        });


        if (profile.getState() == EnumProfileState.PLAYING && item != null && item.getType() == Material.ENDER_PEARL) {
            if (profile.getMatch().getMatchState() == EnumMatchState.STARTING) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(CC.translate("&cYou cannot use ender pearls during the starting phase."));
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

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING) {
            event.setCancelled(true);
        }
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

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (profile.getState() == EnumProfileState.SPECTATING) {
            event.setCancelled(true);
            return;
        }

        if (profile.getState() == EnumProfileState.PLAYING) {
            event.getItemDrop().remove();
        }
    }
}
