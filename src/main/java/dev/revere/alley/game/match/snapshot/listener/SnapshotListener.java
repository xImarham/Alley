package dev.revere.alley.game.match.snapshot.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.snapshot.ISnapshotDataService;
import dev.revere.alley.game.match.snapshot.SnapshotDataService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import java.util.UUID;

/**
 * @author Emmy
 * @project alley-practice
 * @since 01/07/2025
 */
public class SnapshotListener implements Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void onPotionSplashEvent(PotionSplashEvent event) {
        if (!(event.getPotion().getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) event.getPotion().getShooter();
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(shooter.getUniqueId());

        EnumProfileState profileState = profile.getState();
        if (profileState != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null || match.getState() != EnumMatchState.RUNNING) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = match.getGamePlayer(shooter);

        if (event.getIntensity(shooter) <= 0.5D) {
            gamePlayer.getData().incrementMissedPotions();
        }
    }

    @EventHandler
    private void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof ThrownPotion)) {
            return;
        }

        Player shooter = (Player) event.getEntity().getShooter();
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(shooter.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = profile.getMatch();
        if (match == null || match.getState() != EnumMatchState.RUNNING) {
            return;
        }

        MatchGamePlayerImpl gamePlayer = match.getGamePlayer(shooter);
        gamePlayer.getData().incrementThrownPotions();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player attacker = (Player) event.getDamager();
        Player defender = (Player) event.getEntity();

        Profile attackerProfile = Alley.getInstance().getService(IProfileService.class).getProfile(attacker.getUniqueId());
        Profile defenderProfile = Alley.getInstance().getService(IProfileService.class).getProfile(defender.getUniqueId());

        if (attackerProfile.getState() != EnumProfileState.PLAYING ||
                defenderProfile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        AbstractMatch match = attackerProfile.getMatch();
        if (match == null || match != defenderProfile.getMatch() || match.getState() != EnumMatchState.RUNNING) {
            return;
        }

        MatchGamePlayerImpl attackerGamePlayer = match.getGamePlayer(attacker);
        MatchGamePlayerImpl defenderGamePlayer = match.getGamePlayer(defender);

        boolean isCritical = !attacker.isOnGround() && attacker.getFallDistance() > 0;
        if (isCritical) {
            attackerGamePlayer.getData().incrementCriticalHits();
        }

        boolean isBlocked = defender.isBlocking();
        if (isBlocked) {
            defenderGamePlayer.getData().incrementBlockedHits();
        }

        UUID attackerId = attacker.getUniqueId();

        ISnapshotDataService snapshotDataService = Alley.getInstance().getService(ISnapshotDataService.class);
        if (snapshotDataService.isWTap(attackerId)) {
            attackerGamePlayer.getData().incrementWTaps();
            snapshotDataService.resetSprint(attackerId);
        }
    }
}