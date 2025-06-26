package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.impl.StandAloneArena;
import dev.revere.alley.base.kit.setting.impl.mechanic.KitSettingBuildImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingBedImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingSpleefImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.player.enums.EnumBaseRaiderRole;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.ListenerUtil;
import dev.revere.alley.util.RayTracerUtil;
import dev.revere.alley.util.chat.CC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emmy
 * @project Alley
 * @since 08/02/2025
 */
public class MatchBlockListener implements Listener {
    protected final Alley plugin;

    /**
     * Constructor for the MatchBlockListener class.
     *
     * @param plugin The Alley instance
     */
    public MatchBlockListener(Alley plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        AbstractMatch match = profile.getMatch();
        if (match == null) {
            return;
        }

        GameParticipant<MatchGamePlayerImpl> participant = match.getParticipant(player);

        switch (profile.getState()) {
            case PLAYING:
                EnumMatchState matchState = match.getState();
                if (matchState == EnumMatchState.STARTING || matchState == EnumMatchState.ENDING_MATCH || matchState == EnumMatchState.RESTARTING_ROUND) {
                    event.setCancelled(true);
                    return;
                }

                if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class) && match.getKit().isSettingEnabled(KitSettingRaidingImpl.class)) {
                    if (participant.getPlayer().getData().getRole() == EnumBaseRaiderRole.TRAPPER) {
                        // event.setCancelled(false);
                        match.addBlockToBrokenBlocksMap(event.getBlock().getState(), event.getBlock().getLocation());
                        return;
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }

                if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    if (match.getKit().isSettingEnabled(KitSettingBedImpl.class)) {
                        MatchBedImpl matchBed = (MatchBedImpl) profile.getMatch();
                        if (matchBed == null) {
                            event.setCancelled(true);
                            return;
                        }

                        Block block = event.getBlock();
                        if (!ListenerUtil.isBedFightProtectedBlock(block.getType())) {
                            if (!matchBed.isNearBed(block)) {
                                event.setCancelled(true);
                                return;
                            }

                            event.setCancelled(true);
                            return;
                        }

                        StandAloneArena arena = (StandAloneArena) match.getArena();
                        if (block.getType() != Material.BED_BLOCK) {
                            return;
                        }

                        if (!arena.isEnemyBed(block, participant)) {
                            player.sendMessage(CC.translate("You cannot break your own bed!"));
                            event.setCancelled(true);
                            return;
                        }

                        GameParticipant<MatchGamePlayerImpl> opponent = matchBed.getParticipantA().containsPlayer(player.getUniqueId())
                                ? matchBed.getParticipantB()
                                : matchBed.getParticipantA();

                        if (opponent == null) {
                            event.setCancelled(true);
                            return;
                        }

                        match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                        opponent.setBedBroken(true);
                        matchBed.alertBedDestruction(player, opponent);
                        return;
                    }

                    BlockState blockState = event.getBlock().getState();

                    if (match.getPlacedBlocks().containsKey(blockState)) {
                        match.removeBlockFromPlacedBlocksMap(blockState, event.getBlock().getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }

                if (match.getKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                    Block block = event.getBlock();
                    if (block.getType() != Material.SNOW_BLOCK) {
                        event.setCancelled(true);
                        return;
                    }

                    match.addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
                    event.getBlock().setType(Material.AIR);

                    int amount = ThreadLocalRandom.current().nextInt(3, 6);
                    if (amount > 0) {
                        ItemStack snowballs = new ItemStack(Material.SNOW_BALL, amount);
                        player.getInventory().addItem(snowballs);
                    }
                    return;
                }

                event.setCancelled(true);
                break;

            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());
        int blockY = event.getBlock().getLocation().getBlockY();

        AbstractMatch match = profile.getMatch();
        if (match == null) {
            return;
        }

        switch (profile.getState()) {
            case PLAYING:
                if (match.getState() == EnumMatchState.STARTING) event.setCancelled(true);
                if (match.getState() == EnumMatchState.ENDING_MATCH) event.setCancelled(true);
                if (match.getState() == EnumMatchState.RESTARTING_ROUND) event.setCancelled(true);

                if (match.getArena() instanceof StandAloneArena) {
                    StandAloneArena arena = (StandAloneArena) match.getArena();
                    Location blockLocation = event.getBlock().getLocation();

                    if (blockLocation.getBlockY() > arena.getHeightLimit()) {
                        player.sendMessage(CC.translate("&cYou cannot place blocks above the height limit!"));
                        event.setCancelled(true);
                        return;
                    }

                    if ((arena.getTeam1Portal() != null && blockLocation.distance(arena.getTeam1Portal()) <= arena.getPortalRadius()) ||
                            (arena.getTeam2Portal() != null && blockLocation.distance(arena.getTeam2Portal()) <= arena.getPortalRadius())) {
                        player.sendMessage(CC.translate("&cYou cannot build near a portal!"));
                        event.setCancelled(true);
                        return;
                    }
                }

                if (match.getKit().isSettingEnabled(KitSettingRaidingImpl.class) && match.getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    GameParticipant<MatchGamePlayerImpl> participant = match.getParticipant(player);
                    if (participant.getPlayer().getData().getRole() == EnumBaseRaiderRole.TRAPPER) {
                        // event.setCancelled(false);
                        match.addBlockToPlacedBlocksMap(event.getBlock().getState(), event.getBlock().getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                } else if (match.getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    match.addBlockToPlacedBlocksMap(event.getBlock().getState(), event.getBlockPlaced().getLocation());
                    return;
                }

                event.setCancelled(true);
                break;
            case SPECTATING:
                event.setCancelled(true);
                break;
        }
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntityType() == EntityType.SNOWBALL && event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

            if (profile.getState() != EnumProfileState.PLAYING) return;
            if (profile.getMatch().getState() != EnumMatchState.RUNNING) return;
            if (!profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)) return;

            Snowball snowball = (Snowball) event.getEntity();
            Location hitLocation = RayTracerUtil.rayTrace(snowball.getLocation(), snowball.getVelocity().normalize());

            if (hitLocation.getBlock().getType() == Material.SNOW || hitLocation.getBlock().getType() == Material.SNOW_BLOCK) {
                profile.getMatch().addBlockToBrokenBlocksMap(hitLocation.getBlock().getState(), hitLocation);
                hitLocation.getBlock().setType(Material.AIR);
            }
        }
    }

    @EventHandler
    private void onDoorOpen(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = this.plugin.getProfileService().getProfile(player.getUniqueId());

        if (profile.getState() != EnumProfileState.PLAYING) return;
        if (profile.getMatch() == null) return;
        if (profile.getMatch().getState() != EnumMatchState.RUNNING) return;
        if (profile.getMatch().getKit().isSettingEnabled(KitSettingRaidingImpl.class)) {
            Block block = event.getClickedBlock();
            if (block == null || block.getType() == Material.AIR) return;

            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK) {
                if (block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
                    BlockState state = block.getState();
                    this.tpUpIfSignInteractionPresent(event, state, profile, player);
                }

                GameParticipant<MatchGamePlayerImpl> participant = profile.getMatch().getParticipant(player);
                if (participant.getPlayer().getData().getRole() == EnumBaseRaiderRole.RAIDER && ListenerUtil.isDoorOrGate(block.getType())) {
                    if (event.getPlayer().isSneaking() && event.getItem().getType() == Material.ENDER_PEARL && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock().getType().name().contains("FENCE")) {
                        return;
                    }
                    event.setCancelled(true);
                    player.sendMessage(CC.translate("&cYou cannot open doors or gates as a raider!"));
                }
            }
        } else {
            if (event.getClickedBlock() == null) {
                return;
            }

            if (ListenerUtil.isDoorOrGate(event.getClickedBlock().getType())) {
                event.setCancelled(true);
                player.sendMessage(CC.translate("&cYou cannot open doors or gates during a match!"));
            }
        }
    }

    /**
     * Teleports the player up if a sign with the "[elevator]" in its first line is present.
     *
     * @param event   The PlayerInteractEvent
     * @param state   The BlockState of the clicked block
     * @param profile The Profile of the player
     * @param player  The Player who interacted with the block
     */
    private void tpUpIfSignInteractionPresent(PlayerInteractEvent event, BlockState state, Profile profile, Player player) {
        if (state instanceof Sign) {
            Sign sign = (Sign) state;
            String[] lines = sign.getLines();
            if (lines.length > 0 && lines[0].equalsIgnoreCase("[elevator]")) {
                Location pos1 = profile.getMatch().getArena().getPos1();
                Location tpLocation = pos1.clone();
                for (int y = tpLocation.getBlockY() + 1; y < tpLocation.getWorld().getMaxHeight(); y++) {
                    if (tpLocation.getWorld().getBlockAt(tpLocation.getBlockX(), y, tpLocation.getBlockZ()).getType() == Material.AIR) {
                        tpLocation.setY(y);
                        player.teleport(tpLocation);
                        break;
                    }
                }

                event.setCancelled(true);
            }
        }
    }
}