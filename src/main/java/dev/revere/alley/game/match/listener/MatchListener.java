package dev.revere.alley.game.match.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingDenyMovementImpl;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingNoHungerImpl;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingVoidDeathImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRoundsImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingSpleefImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingStickFightImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingSumoImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.game.match.utility.MatchUtility;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
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
import org.bukkit.inventory.ItemStack;

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

            if (match.getArena() instanceof StandAloneArena) {
                StandAloneArena arena = (StandAloneArena) match.getArena();
                if (player.getLocation().getY() <= arena.getVoidLevel() && matchKit.isSettingEnabled(KitSettingVoidDeathImpl.class)) {
                    if (player.getGameMode() == GameMode.SPECTATOR) return;
                    if (player.getGameMode() == GameMode.CREATIVE) return;
                    if (match.getArena().getType() != EnumArenaType.STANDALONE) return;

                    if (match.getKit().isSettingEnabled(KitSettingStickFightImpl.class)) {
                        MatchRoundsImpl roundsMatch = (MatchRoundsImpl) match;
                        roundsMatch.handleDeath(player);
                        return;
                    }

                    profile.getMatch().handleDeath(player);
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

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.PLAYING) return;

        event.setDeathMessage(null);

        profile.getMatch().handleDeathItemDrop(player, event);

        this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> player.spigot().respawn(), 1L);

        profile.getMatch().handleDeath(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() == null) return;

        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() == EnumProfileState.SPECTATING) {
            if (!Menu.currentlyOpenedMenus.containsKey(player.getName())) {
                event.setCancelled(true);
            }
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
            if (ListenerUtil.isSword(event.getItemDrop().getItemStack().getType())) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou cannot drop your sword during this match."));
                return;
            }
        }
        ListenerUtil.clearDroppedItemsOnRegularItemDrop(event.getItemDrop());
    }

    @EventHandler
    private void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        if (profile.getState() != EnumProfileState.PLAYING) {
            return;
        }

        ItemStack item = event.getItem();
        if (item.getType() == Material.POTION) {
            this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                player.getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1));
                player.updateInventory();
            }, 1L);
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
            if (match.getKit().isSettingEnabled(KitSettingRoundsImpl.class) /*|| profile.getMatch().getKit().isSettingEnabled(KitSettingBridgesImpl.class)*/) {
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
