package dev.revere.alley.game.match.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.impl.StandAloneArena;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.kit.setting.impl.mechanic.KitSettingDenyMovementImpl;
import dev.revere.alley.feature.kit.setting.impl.mechanic.KitSettingNoHungerImpl;
import dev.revere.alley.feature.kit.setting.impl.mode.*;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.impl.kit.MatchStickFightImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.utility.MatchUtility;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.reflection.impl.ActionBarReflectionService;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class MatchListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchListener class.
     *
     * @param plugin The plugin instance.
     */
    public MatchListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
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
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        AbstractMatch match = profile.getMatch();
        if (match == null) return;

        Kit matchKit = match.getKit();
        if (profile.getState() == EnumProfileState.PLAYING && profile.getMatch().getState() == EnumMatchState.RUNNING) {
            if (matchKit.isSettingEnabled(KitSettingSumoImpl.class) || matchKit.isSettingEnabled(KitSettingSpleefImpl.class)) {
                if (player.getLocation().getBlock().getType() == Material.WATER || player.getLocation().getBlock().getType() == Material.STATIONARY_WATER) {
                    player.setHealth(0);
                }
            }

            if (player.getLocation().getY() <= this.plugin.getConfigService().getSettingsConfig().getInt("game.death-y-level")) {
                if (player.getGameMode() == GameMode.SPECTATOR) return;
                if (player.getGameMode() == GameMode.CREATIVE) return;

                if (match.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
                    MatchStickFightImpl matchStickFight = (MatchStickFightImpl) match;
                    matchStickFight.handleDeath(player);
                    return;
                }

                if (matchKit.isSettingEnabled(KitSettingLivesImpl.class) || matchKit.isSettingEnabled(KitSettingBattleRushImpl.class)) {
                    player.setHealth(0);
                    player.setAllowFlight(true);
                    player.setFlying(true);
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        }

        if (profile.getState() == EnumProfileState.PLAYING) {
            if (match.getState() == EnumMatchState.STARTING || match.getState() == EnumMatchState.ENDING_ROUND || match.getState() == EnumMatchState.RESTARTING_ROUND) {
                if (matchKit.isSettingEnabled(KitSettingDenyMovementImpl.class)) {
                    List<GameParticipant<MatchGamePlayerImpl>> participants = match.getParticipants();
                    match.denyPlayerMovement(participants);
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

    @EventHandler
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.PLAYING) {
            event.setRespawnLocation(player.getLocation());
        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = Alley.getInstance().getCombatService().getLastAttacker(player);

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.PLAYING) return;

        event.setDeathMessage(null);

        if (killer != null) {
            GameParticipant<MatchGamePlayerImpl> killerParticipant = profile.getMatch().getParticipant(killer);
            killerParticipant.getPlayer().getData().incrementKills();

            Alley.getInstance().getReflectionRepository().getReflectionService(ActionBarReflectionService.class).sendDeathMessage(killer, player);
            profile.getMatch().getParticipants()
                    .forEach(participant -> participant.getPlayer().getPlayer().sendMessage(CC.translate("&c" + player.getName() + " &fwas killed by &c" + killer.getName() + "&f.")));
            profile.getMatch().createSnapshot(player.getUniqueId(), killer.getUniqueId());
            //PlayerUtil.resetLastAttacker(player);
        } else {
            profile.getMatch().getParticipants()
                    .forEach(participant -> participant.getPlayer().getPlayer().sendMessage(CC.translate("&c" + player.getName() + " &fdied.")));
        }

        ListenerUtil.clearDroppedItemsOnDeath(event, player);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> player.spigot().respawn(), 1L);

        profile.getMatch().handleDeath(player);

        if (!profile.getMatch().getParticipant(player).isAllDead()) {
            Kit matchKit = profile.getMatch().getKit();
            if (matchKit.isSettingEnabled(KitSettingLivesImpl.class)
                    || matchKit.isSettingEnabled(KitSettingBattleRushImpl.class)
                    || matchKit.isSettingEnabled(KitSettingStickFightImpl.class)) {
                return;
            }

            profile.getMatch().addSpectator(player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

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
            Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
            if (profile.getState() != EnumProfileState.PLAYING) return;

            if (profile.getMatch().getKit().isSettingEnabled(KitSettingNoHungerImpl.class)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPortal(PlayerPortalEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.PLAYING) {
            MatchRoundsImpl match = (MatchRoundsImpl) profile.getMatch();
            if (match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class) /*|| profile.getMatch().getKit().isSettingEnabled(KitSettingBridgesImpl.class)*/) {
                if (player.getGameMode() == GameMode.CREATIVE) return;
                if (player.getGameMode() == GameMode.SPECTATOR) return;
                if (player.getLocation().getBlock().getType() == Material.ENDER_PORTAL || player.getLocation().getBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                    StandAloneArena arena = (StandAloneArena) match.getArena();
                    GameParticipant<MatchGamePlayerImpl> playerTeam = match.getParticipantA().containsPlayer(player.getUniqueId())
                            ? match.getParticipantA()
                            : match.getParticipantB();

                    if (!arena.isEnemyPortal(match, player.getLocation(), playerTeam)) {
                        player.sendMessage(CC.translate("&cYou cannot enter your own portal!"));
                        player.setHealth(0);
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.setGameMode(GameMode.SPECTATOR);
                        return;
                    }

                    if (match.getState() == EnumMatchState.ENDING_ROUND || match.getState() == EnumMatchState.ENDING_MATCH || match.getState() == EnumMatchState.RESTARTING_ROUND) {
                        return;
                    }

                    GameParticipant<MatchGamePlayerImpl> opponent = match.getParticipantA().containsPlayer(player.getUniqueId()) ? match.getParticipantB() : match.getParticipantA();
                    opponent.getPlayers().forEach(matchGamePlayer -> matchGamePlayer.setDead(true));
                    opponent.setEliminated(true);

                    if (match.canEndRound()) {

                        match.setScorer(player.getName());
                        match.handleRoundEnd();

                        if (match.canEndMatch()) {
                            Location spawnLocation = match.getParticipantA().containsPlayer(player.getUniqueId()) ? match.getArena().getPos1() : match.getArena().getPos2();
                            player.teleport(spawnLocation);

                            match.setEndTime(System.currentTimeMillis());
                            match.setState(EnumMatchState.ENDING_MATCH);
                            match.getRunnable().setStage(4);
                        }
                    }
                }
            }
        }
    }
}