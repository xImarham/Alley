package dev.revere.alley.feature.explosives.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingExplosiveImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBedImpl;
import dev.revere.alley.feature.explosives.ExplosiveService;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Remi
 * @project Alley
 * @since 24/06/2025
 */
public class ExplosiveListener implements Listener {
    protected final Alley plugin;

    private static final String PRACTICE_TNT_METADATA = "PRACTICE_TNT";

    /**
     * Constructor for the ExplosiveListener class.
     *
     * @param plugin the Alley plugin instance.
     */
    public ExplosiveListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item == null) return;

        Material itemType = item.getType();
        Action action = event.getAction();

        if (itemType == Material.FIREBALL && (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)) {
            handleFireballUse(event);
        } else if (itemType == Material.TNT && action == Action.RIGHT_CLICK_BLOCK) {
            handleTntPlace(event);
        }
    }

    private void handleFireballUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile == null) return;
        if (profile.getState() != EnumProfileState.PLAYING) return;

        AbstractMatch match = profile.getMatch();
        if (match == null) return;

        if (!match.getKit().isSettingEnabled(KitSettingExplosiveImpl.class)) return;

        ItemStack item = event.getItem();
        if (item == null || item.getType() != Material.FIREBALL) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        event.setCancelled(true);

        EnumCooldownType cooldownType = EnumCooldownType.FIREBALL;
        CooldownRepository cooldownRepository = this.plugin.getCooldownRepository();
        Optional<Cooldown> optionalCooldown = Optional.ofNullable(cooldownRepository.getCooldown(player.getUniqueId(), cooldownType));
        if (optionalCooldown.isPresent() && optionalCooldown.get().isActive()) {
            player.sendMessage(CC.translate("&cYou must wait " + optionalCooldown.get().remainingTimeInMinutes() + " &cbefore using another fireball."));
            return;
        }

        Cooldown cooldown = optionalCooldown.orElseGet(() -> {
            Cooldown newCooldown = new Cooldown(cooldownType, () -> player.sendMessage(CC.translate("&cYou can now use fireballs again.")));
            cooldownRepository.addCooldown(player.getUniqueId(), cooldownType, newCooldown);
            return newCooldown;
        });

        cooldown.resetCooldown();

        ExplosiveService explosiveService = this.plugin.getExplosiveService();
        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(false);
        fireball.setYield(2.0F);
        fireball.setVelocity(player.getLocation().getDirection().normalize().multiply(explosiveService.getSpeed()));

        //SoundUtil.playCustomSound(player, Sound.GHAST_FIREBALL, 1.0f, 1.0f);

        if (player.getGameMode() == GameMode.CREATIVE) return;

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }

    private void handleTntPlace(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile == null || profile.getState() != EnumProfileState.PLAYING) return;

        AbstractMatch match = profile.getMatch();
        if (match == null || !match.getKit().isSettingEnabled(KitSettingExplosiveImpl.class)) return;

        event.setCancelled(true);

        if (player.getGameMode() != GameMode.CREATIVE) {
            ItemStack item = event.getItem();
            if (item.getAmount() > 1) {
                item.setAmount(item.getAmount() - 1);
            } else {
                player.getInventory().setItemInHand(null);
            }
            player.updateInventory();
        }

        Location tntLocation = clickedBlock.getRelative(event.getBlockFace()).getLocation().add(0.5, 0.0, 0.5);
        TNTPrimed tnt = (TNTPrimed) tntLocation.getWorld().spawnEntity(tntLocation, EntityType.PRIMED_TNT);

        tnt.setFuseTicks(this.plugin.getExplosiveService().getTntFuseTicks());
        tnt.setMetadata(PRACTICE_TNT_METADATA, new FixedMetadataValue(plugin, true));
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball) || !(event.getEntity().getShooter() instanceof Player)) return;

        Fireball fireball = (Fireball) event.getEntity();
        fireball.getWorld().createExplosion(fireball.getLocation(), 0F, false);
        applyPlayerKnockback(fireball, fireball.getLocation());
    }

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);

        if (event.getEntityType() != EntityType.PRIMED_TNT || !event.getEntity().hasMetadata(PRACTICE_TNT_METADATA)) {
            return;
        }

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        Location explosionLocation = tnt.getLocation();

        handleCustomTntExplosion(tnt, explosionLocation);
    }

    @EventHandler
    public void onExplosionDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        EntityDamageEvent.DamageCause cause = event.getCause();
        if (cause != EntityDamageEvent.DamageCause.BLOCK_EXPLOSION && cause != EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            return;
        }

        Player player = (Player) event.getEntity();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile != null && profile.getState() == EnumProfileState.PLAYING) {
            event.setDamage(0.0);
        }
    }

    /**
     * Applies configured knockback to all players within range of an explosion.
     *
     * @param source            The entity causing the explosion (e.g., Fireball, TNTPrimed).
     * @param explosionLocation The center location of the explosion.
     */
    private void applyPlayerKnockback(Entity source, Location explosionLocation) {
        double range = this.plugin.getExplosiveService().getRange();
        double horizontal = this.plugin.getExplosiveService().getHorizontal();
        double vertical = this.plugin.getExplosiveService().getVertical();

        source.getNearbyEntities(range, range, range).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
                if (profile == null || profile.getState() != EnumProfileState.PLAYING) return;

                Vector knockback = player.getLocation().toVector().subtract(explosionLocation.toVector()).normalize();
                if (knockback.lengthSquared() == 0) {
                    knockback.setY(1);
                }

                knockback.multiply(horizontal).setY(vertical);
                player.setVelocity(knockback);
            }
        });
    }

    /**
     * Handles the logic for a custom TNT explosion.
     * <p>
     * This explosion operates in a spherical radius and specifically targets
     * and destroys only WOOD and ENDER_STONE blocks. It then creates a
     * purely cosmetic (zero-power) explosion for sounds and particle effects
     * and applies knockback to nearby players.
     *
     * @param tnt The TNTPrimed entity that is exploding. This is used as a reference for applying player knockback.
     * @param explosionLocation The central Location where the explosion occurs.
     */
    private void handleCustomTntExplosion(TNTPrimed tnt, Location explosionLocation) {
        double range = this.plugin.getExplosiveService().getRange();

        List<Block> blocksToBreak = new ArrayList<>();
        int radius = (int) Math.ceil(range);

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location blockLoc = explosionLocation.clone().add(x, y, z);
                    if (blockLoc.distance(explosionLocation) <= range) {
                        Block block = blockLoc.getBlock();
                        Material type = block.getType();

                        if (type == Material.WOOD || type == Material.ENDER_STONE) {
                            blocksToBreak.add(block);
                        }
                    }
                }
            }
        }

        for (Block block : blocksToBreak) {
            block.setType(Material.AIR);
        }

        explosionLocation.getWorld().createExplosion(explosionLocation, 0F, false);

        applyPlayerKnockback(tnt, explosionLocation);
    }
}