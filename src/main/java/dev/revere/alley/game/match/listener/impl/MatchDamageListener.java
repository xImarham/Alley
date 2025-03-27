package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.settings.impl.*;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchDamageListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchDamageListener class.
     *
     * @param plugin The Alley instance
     */
    public MatchDamageListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

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

            Kit matchKit = profile.getMatch().getKit();
            if (matchKit.isSettingEnabled(KitSettingBoxingImpl.class)
                    || matchKit.isSettingEnabled(KitSettingSumoImpl.class)
                    || matchKit.isSettingEnabled(KitSettingSpleefImpl.class)
                    || matchKit.isSettingEnabled(KitSettingNoDamageImpl.class)
                    || matchKit.isSettingEnabled(KitSettingLivesImpl.class)
                    || matchKit.isSettingEnabled(KitSettingBattleRushImpl.class)
                    || matchKit.isSettingEnabled(KitSettingStickFightImpl.class)) {
                event.setDamage(0);
                player.setHealth(20.0);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    private void handlePearlDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof EnderPearl) {
            EnderPearl enderPearl = (EnderPearl) event.getDamager();
            if (enderPearl.getShooter() instanceof Player) {
                Player player = (Player) enderPearl.getShooter();
                Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
                if (profile.getState() == EnumProfileState.PLAYING) {
                    Kit matchKit = profile.getMatch().getKit();
                    if (matchKit.isSettingEnabled(KitSettingBoxingImpl.class)
                            || matchKit.isSettingEnabled(KitSettingSumoImpl.class)
                            || matchKit.isSettingEnabled(KitSettingSpleefImpl.class)
                            || matchKit.isSettingEnabled(KitSettingNoDamageImpl.class)
                            || matchKit.isSettingEnabled(KitSettingLivesImpl.class)
                            || matchKit.isSettingEnabled(KitSettingBattleRushImpl.class)
                            || matchKit.isSettingEnabled(KitSettingStickFightImpl.class)) {
                        event.setCancelled(true);
                    }
                }
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
            Profile damagedprofile = this.plugin.getProfileService().getProfile(damaged.getUniqueId());
            Profile attackerProfile = this.plugin.getProfileService().getProfile(attacker.getUniqueId());

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

                if (match.isInSameTeam(attacker, damaged)) {
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

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntityDamageByEntityMonitor(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Profile profile = this.plugin.getProfileService().getProfile(event.getEntity().getUniqueId());
            if (profile.getState() == EnumProfileState.SPECTATING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getState() == EnumProfileState.PLAYING) {
                Player player = (Player) event.getEntity();
                Player attacker = (Player) event.getDamager();
                Alley.getInstance().getCombatService().setLastAttacker(player, attacker);
            }
        }
    }
}