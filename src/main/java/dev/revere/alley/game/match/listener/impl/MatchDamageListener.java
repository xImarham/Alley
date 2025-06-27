package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.visual.KitSettingBowShotIndicatorImpl;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingNoDamageImpl;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingNoFallDamageImpl;
import dev.revere.alley.base.kit.setting.impl.mode.*;
import dev.revere.alley.base.kit.setting.impl.visual.KitSettingHealthBarImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.chat.Symbol;
import org.bukkit.entity.Arrow;
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
            Kit matchKit = profile.getMatch().getKit();

            if (matchKit.isSettingEnabled(KitSettingNoFallDamageImpl.class)
                    && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                event.setCancelled(true);
            }

            if (profile.getMatch().getState() != EnumMatchState.RUNNING) {
                event.setCancelled(true);
                return;
            }

            if (profile.getMatch().getGamePlayer(player).isDead()) {
                event.setCancelled(true);
                return;
            }

            if (matchKit.isSettingEnabled(KitSettingBoxingImpl.class)
                    || matchKit.isSettingEnabled(KitSettingSumoImpl.class)
                    || matchKit.isSettingEnabled(KitSettingSpleefImpl.class)
                    || matchKit.isSettingEnabled(KitSettingNoDamageImpl.class)) {
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
                            || matchKit.isSettingEnabled(KitSettingRoundsImpl.class)
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
                if (match == null) {
                    return;
                }

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

                GameParticipant<MatchGamePlayerImpl> participant = match.getParticipant(attacker);
                GameParticipant<MatchGamePlayerImpl> opponent = match.getParticipant(damaged);

                participant.setTeamHits(participant.getTeamHits() + 1);

                if (match.getKit().isSettingEnabled(KitSettingBowShotIndicatorImpl.class) && event.getDamager() instanceof Arrow) {
                    double finalHealth = damaged.getHealth() - event.getFinalDamage();
                    finalHealth = Math.max(0, finalHealth);

                    if (finalHealth > 0) {
                        attacker.sendMessage(CC.translate(this.plugin.getCoreAdapter().getCore().getPlayerColor(damaged) + damaged.getName() + " &7&l" + Symbol.ARROW_R + " &6" + String.format("%.1f", finalHealth) + " &c" + Symbol.HEART));
                    }
                }

                if (match.getKit().isSettingEnabled(KitSettingBoxingImpl.class)) {
                    int lowestPlayerCount = match.getParticipants().stream()
                            .mapToInt(p -> p.getPlayers().size())
                            .filter(size -> size > 0)
                            .min()
                            .orElse(1);

                    int requiredHits = lowestPlayerCount * 100;

                    if (participant.getTeamHits() >= requiredHits) {
                        opponent.getPlayers().forEach(matchGamePlayer -> {
                            match.handleDeath(matchGamePlayer.getPlayer(), EntityDamageEvent.DamageCause.ENTITY_ATTACK);
                        });
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

                this.plugin.getCombatService().setLastAttacker(player, attacker);

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingHealthBarImpl.class)) {
                    this.plugin.getReflectionRepository().getReflectionService(ActionBarReflectionService.class)
                            .visualizeTargetHealth(attacker, player);
                }
            }
        }
    }
}