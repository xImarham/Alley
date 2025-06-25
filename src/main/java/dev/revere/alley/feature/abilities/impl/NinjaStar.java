package dev.revere.alley.feature.abilities.impl;

import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.AbstractAbility;
import dev.revere.alley.feature.abilities.utils.DurationFormatter;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class NinjaStar extends AbstractAbility {
    private final Alley plugin = Alley.getInstance();
    private final Map<UUID, UUID> TAGGED = Maps.newHashMap();

    public NinjaStar() {
        super("NINJA_STAR");
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();

            //AbilityCooldowns.addCooldown("TELEPORT", victim, 15);
            TAGGED.put(victim.getUniqueId(), damager.getUniqueId());
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);

            Player player = event.getPlayer();
            Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

            if (profile.getNinjastar().onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lNinja Star &7cooldown for &4" + DurationFormatter.getRemaining(profile.getNinjastar().getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if (profile.getPartneritem().onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            //if (!AbilityCooldowns.isOnCooldown("TELEPORT", player)) return;

            PlayerUtil.decrement(player);

            Player target = Bukkit.getPlayer(TAGGED.get(player.getUniqueId()));

            profile.getNinjastar().applyCooldown(player, 60 * 1000);
            profile.getPartneritem().applyCooldown(player, 10 * 1000);

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (target == null) {
                        return;
                    }
                    player.teleport(target.getLocation());
                    player.sendMessage(CC.translate("&7You have been successfully teleported")); // you just got teleported back
                }
            }.runTaskLaterAsynchronously(this.plugin, (5 * 10));

            plugin.getAbilityService().cooldownExpired(player, this.getName(), this.getAbility());
            plugin.getAbilityService().playerMessage(player, this.getAbility());
            plugin.getAbilityService().targetMessage(target, player, this.getAbility());

            TAGGED.remove(player.getUniqueId());
        }
    }
}