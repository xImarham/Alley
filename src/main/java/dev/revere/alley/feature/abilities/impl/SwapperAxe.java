package dev.revere.alley.feature.abilities.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.AbstractAbility;
import dev.revere.alley.feature.abilities.utils.DurationFormatter;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.TaskUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SwapperAxe extends AbstractAbility {
    private final Alley plugin = Alley.getInstance();

    public SwapperAxe() {
        super("SWAPPER_AXE");
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            Profile profile = Alley.getInstance().getProfileService().getProfile(damager.getUniqueId());

            if (!isAbility(damager.getItemInHand())) return;

            if (profile.getSwapperaxe().onCooldown(damager)) {
                damager.sendMessage(CC.translate("&fYou are on &6&lSwapper Axe &7cooldown for &4" + DurationFormatter.getRemaining(profile.getSwapperaxe().getRemainingMillis(damager), true, true)));
                damager.updateInventory();
                return;
            }

            if (profile.getPartneritem().onCooldown(damager)) {
                damager.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getPartneritem().getRemainingMillis(damager), true, true)));
                damager.updateInventory();
                return;
            }

            Player victim = (Player) event.getEntity();
            ItemStack helmet = victim.getInventory().getHelmet();

            if (helmet == null || !helmet.getType().equals(Material.DIAMOND_HELMET)) return;

            this.onSwapperAxe(victim, damager, helmet);

            profile.getSwapperaxe().applyCooldown(damager, 60 * 1000);
            profile.getPartneritem().applyCooldown(damager, 10 * 1000);

            plugin.getAbilityService().cooldownExpired(damager, this.getName(), this.getAbility());
            plugin.getAbilityService().playerMessage(damager, this.getAbility());
        }
    }

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            Player player = event.getPlayer();

            if (this.hasCooldown(player)) {
                event.setCancelled(true);
                plugin.getAbilityService().cooldown(player, this.getName(), this.getCooldown(player));
                player.updateInventory();
            }
        }
    }

    private void onSwapperAxe(Player victim, Player damager, ItemStack helmet) {
        plugin.getAbilityService().targetMessage(victim, damager, this.getAbility());

        TaskUtil.runLaterAsync(() -> {
            victim.getInventory().addItem(helmet);
            victim.getInventory().setHelmet(null);
            victim.updateInventory();
        }, 5 * 20L);
    }
}