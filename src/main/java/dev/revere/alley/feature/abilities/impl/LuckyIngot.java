package dev.revere.alley.feature.abilities.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.abilities.AbstractAbility;
import dev.revere.alley.feature.abilities.IAbilityService;
import dev.revere.alley.feature.abilities.utils.DurationFormatter;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumGlobalCooldown;
import dev.revere.alley.util.PlayerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Hysteria Development
 * @project Practice
 * @date 2/15/2023
 */
public class LuckyIngot extends AbstractAbility {
    private final Alley plugin = Alley.getInstance();

    public LuckyIngot() {
        super("LUCKY_INGOT");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!isAbility(event.getItem())) return;

        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);

            Player player = event.getPlayer();

            IProfileService profileService = Alley.getInstance().getService(IProfileService.class);
            IAbilityService abilityService = Alley.getInstance().getService(IAbilityService.class);

            Profile profile = profileService.getProfile(player.getUniqueId());

            if (profile.getCooldown(LuckyIngot.class).onCooldown(player)) {
                player.sendMessage(CC.translate("&fYou are on &6&lLucky Ingot &7cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(LuckyIngot.class).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            if(profile.getGlobalCooldown(EnumGlobalCooldown.PARTNER_ITEM).onCooldown(player)){
                player.sendMessage(CC.translate("&fYou are on &6&lPartner Item &fcooldown for &6" + DurationFormatter.getRemaining(profile.getGlobalCooldown(EnumGlobalCooldown.PARTNER_ITEM).getRemainingMillis(player), true, true)));
                player.updateInventory();
                return;
            }

            PlayerUtil.decrement(player);

            profile.getCooldown(LuckyIngot.class).applyCooldown(player, 60 * 1000);
            profile.getGlobalCooldown(EnumGlobalCooldown.PARTNER_ITEM).applyCooldown(player,  10 * 1000);

            this.giveRandomEffect(player);

            abilityService.sendCooldownExpiredMessage(player, this.getName(), this.getAbility());
            abilityService.sendPlayerMessage(player, this.getAbility());
        }
    }

    @EventHandler
    public void checkCooldown(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());
        if (action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK)) {
            if (!isAbility(player.getItemInHand())) {
                return;
            }
            if (isAbility(player.getItemInHand())) {
                if (this.hasCooldown(player)) {
                    player.sendMessage(CC.translate("&fYou are on cooldown for &4" + DurationFormatter.getRemaining(profile.getCooldown(LuckyIngot.class).getRemainingMillis(player), true)));
                    event.setCancelled(true);
                    player.updateInventory();
                }
            }
        }
    }

    private void giveRandomEffect(Player player) {
        switch (ThreadLocalRandom.current().nextInt(2)) {
            case 0:
                player.removePotionEffect(PotionEffectType.REGENERATION);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 11, 1));

                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 11, 1));
                break;
            case 1:
                player.removePotionEffect(PotionEffectType.WEAKNESS);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 20 * 11, 1));

                player.removePotionEffect(PotionEffectType.WITHER);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 20 * 11, 1));

        }
    }
}
