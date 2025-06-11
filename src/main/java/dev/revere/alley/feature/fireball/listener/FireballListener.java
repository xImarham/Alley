package dev.revere.alley.feature.fireball.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.base.cooldown.Cooldown;
import dev.revere.alley.base.cooldown.CooldownRepository;
import dev.revere.alley.base.cooldown.enums.EnumCooldownType;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingFireballImpl;
import dev.revere.alley.feature.fireball.FireballService;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Optional;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
public class FireballListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the FireballListener class.
     *
     * @param plugin the Alley plugin instance
     */
    public FireballListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onFireballUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile == null) {
            return;
        }

        if (profile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null) {
            return;
        }

        if (!match.getKit().isSettingEnabled(KitSettingFireballImpl.class)) {
            return;
        }

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

        Fireball fireball = player.launchProjectile(Fireball.class);
        fireball.setIsIncendiary(false);
        fireball.setYield(2.0F);
        fireball.setVelocity(player.getLocation().getDirection().normalize().multiply(1.5));

        //SoundUtil.playCustomSound(player, Sound.GHAST_FIREBALL, 1.0f, 1.0f);

        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }

    @EventHandler
    private void onFireballImpact(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Fireball)) return;

        Fireball fireball = (Fireball) event.getEntity();
        fireball.getWorld().createExplosion(fireball.getLocation(), 0F, false);

        FireballService fireballService = this.plugin.getFireballService();
        double range = fireballService.getRange();
        double horizontal = fireballService.getHorizontal();
        double vertical = fireballService.getVertical();

        fireball.getNearbyEntities(range, range, range).forEach(entity -> {
            if (entity instanceof Player) {
                Player player = (Player) entity;

                Vector knockback = player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize();
                knockback.multiply(horizontal).setY(vertical);
                player.setVelocity(knockback);
            }
        });
    }
}