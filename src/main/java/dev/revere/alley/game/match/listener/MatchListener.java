package dev.revere.alley.game.match.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigHandler;
import dev.revere.alley.cooldown.Cooldown;
import dev.revere.alley.cooldown.CooldownRepository;
import dev.revere.alley.cooldown.enums.EnumCooldownType;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.MatchUtility;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.GameParticipant;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.kit.settings.impl.*;
import dev.revere.alley.locale.ErrorMessage;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.ActionBarUtil;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.location.RayTracerUtil;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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
import java.util.concurrent.ThreadLocalRandom;

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
            if (profile.getMatch().getState() != EnumMatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getGamePlayer(player).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getKit().isSettingEnabled(KitSettingBoxingImpl.class)
                    || profile.getMatch().getKit().isSettingEnabled(KitSettingSumoImpl.class)
                    || profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)
                    || profile.getMatch().getKit().isSettingEnabled(KitSettingNoDamageImpl.class)) {
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
                if (damagedprofile.getMatch().getState() != EnumMatchState.RUNNING) {
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

                if (match.getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
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

            if (profile.getState() == EnumProfileState.PLAYING && profile.getMatch().getState() == EnumMatchState.RUNNING &&
                    profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)) {

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
        switch (profile.getState()) {
            case PLAYING:
                if (profile.getMatch().getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    BlockState blockState = event.getBlock().getState();

                    if (profile.getMatch().getPlacedBlocks().containsKey(blockState)) {
                        profile.getMatch().removeBlockFromPlacedBlocksMap(blockState, event.getBlock().getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                    Block block = event.getBlock();
                    if (block.getType() != Material.SNOW_BLOCK) {
                        event.setCancelled(true);
                        return;
                    }

                    event.setCancelled(false);
                    event.getBlock().setType(Material.AIR);

                    int amount = ThreadLocalRandom.current().nextInt(3, 6);
                    if (amount > 0) {
                        ItemStack snowballs = new ItemStack(Material.SNOW_BALL, amount);
                        player.getInventory().addItem(snowballs);
                    }
                    return;
                }

                event.setCancelled(true);
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
                if (profile.getMatch().getState() == EnumMatchState.STARTING) event.setCancelled(true);
                if (profile.getMatch().getState() == EnumMatchState.ENDING_MATCH) return;

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    profile.getMatch().addBlockToPlacedBlocksMap(event.getBlock().getState(), event.getBlockPlaced().getLocation());
                    return;
                }
                event.setCancelled(true);
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
                if (MatchUtility.isBeyondBounds(event.getTo(), profile)) {
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
        AbstractMatch match = profile.getMatch();

        if (profile.getMatch() != null) {
            if (profile.getState() == EnumProfileState.PLAYING && profile.getMatch().getState() == EnumMatchState.RUNNING) {
                if (profile.getMatch().getKit().isSettingEnabled(KitSettingSumoImpl.class) || profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                    if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                        player.setHealth(0);
                    }
                }

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    if (player.getLocation().getY() <= ConfigHandler.getInstance().getSettingsConfig().getInt("game.death-y-level")) {
                        if (player.getGameMode() == GameMode.SPECTATOR) return;
                        if (player.getGameMode() == GameMode.CREATIVE) return;
                        player.setHealth(0);
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setGameMode(GameMode.SPECTATOR);
                    }
                }
            }

            if (profile.getState() == EnumProfileState.PLAYING) {
                if (match.getState() == EnumMatchState.STARTING) {
                    if (match.getKit().isSettingEnabled(KitSettingDenyMovementImpl.class)) {
                        List<GameParticipant<MatchGamePlayerImpl>> participants = match.getParticipants();
                        MatchUtility.denyPlayerMovement(participants, player, match);
                    }
                }
            }

            if (profile.getState() == EnumProfileState.SPECTATING || profile.getState() == EnumProfileState.PLAYING) {
                if (profile.getMatch() == null) {
                    return;
                }

                if (MatchUtility.isBeyondBounds(event.getTo(), profile)) {
                    player.teleport(event.getFrom());
                    player.sendMessage(CC.translate("&cYou cannot leave the arena."));
                }
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
        Player killer = PlayerUtil.getLastAttacker(player);

        if (profile.getState() == EnumProfileState.PLAYING) {
            event.setDeathMessage(null);
            if (killer != null) {
                ActionBarUtil.sendMessage(killer, "&c&lKILL! &f" + player.getName(), 3);
                profile.getMatch().getParticipants()
                        .forEach(participant -> participant.getPlayer().getPlayer().sendMessage(CC.translate("&c" + player.getName() + " &fwas killed by &c" + killer.getName() + "&f.")));
                profile.getMatch().createSnapshot(player.getUniqueId(), killer.getUniqueId());
            } else {
                profile.getMatch().getParticipants()
                        .forEach(participant -> participant.getPlayer().getPlayer().sendMessage(CC.translate("&c" + player.getName() + " &fdied.")));
            }

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

    @EventHandler
    private void handleParkourInteraction(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        if (!profile.getMatch().getKit().isSettingEnabled(KitSettingParkourImpl.class)) {
            return;
        }

        if (event.getAction() != Action.PHYSICAL || event.getClickedBlock() == null || event.getClickedBlock().getType() != Material.STONE_PLATE) {
            return;
        }

        if (profile.getState() == EnumProfileState.PLAYING) {
            profile.getMatch().getParticipants().forEach(participant -> {
                Player participantPlayer = participant.getPlayer().getPlayer();
                if (participantPlayer != null && !participantPlayer.getUniqueId().equals(player.getUniqueId())) {
                    profile.getMatch().handleDeath(participantPlayer);
                }
            });
        }
    }

    @EventHandler
    private void onPlayerPearl(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ItemStack item = event.getItem();

        if (profile.getMatch() == null) {
            return;
        }

        if (profile.getMatch().getState() == EnumMatchState.STARTING && item != null && item.getType() == Material.ENDER_PEARL) {
            event.setCancelled(true);
            player.updateInventory();
            player.sendMessage(CC.translate("&cYou cannot use ender pearls during the starting phase."));
            return;
        }

        if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
            return;
        }

        if (profile.getState() == EnumProfileState.PLAYING && item != null && item.getType() == Material.ENDER_PEARL) {
            CooldownRepository cooldownRepository = Alley.getInstance().getCooldownRepository();
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
            profile.setState(EnumProfileState.LOBBY);
            AbstractMatch match = profile.getMatch();

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    profile.getMatch().getGamePlayer(player).getData().setLives(0);
                    profile.getMatch().handleDeath(player);
                    return;
                }
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

            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.RUNNING) {
                if (profile.getMatch().getKit().isSettingEnabled(KitSettingLivesImpl.class)) {
                    profile.getMatch().getGamePlayer(player).getData().setLives(0);
                    profile.getMatch().handleDeath(player);
                    return;
                }
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

    @EventHandler
    private void onHunger(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

            if (profile.getState() != EnumProfileState.PLAYING) {
                return;
            }

            if (profile.getMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }
}
