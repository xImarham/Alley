package dev.revere.alley.game.match.listener.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBuildImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingSpleefImpl;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.location.RayTracerUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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
        Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());

        switch (profile.getState()) {
            case PLAYING:
                EnumMatchState matchState = profile.getMatch().getState();
                if (matchState == EnumMatchState.STARTING || matchState == EnumMatchState.ENDING_MATCH) {
                    event.setCancelled(true);
                    return;
                }

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    BlockState blockState = event.getBlock().getState();

                    if (profile.getMatch().getPlacedBlocks().containsKey(blockState)) {
                        profile.getMatch().removeBlockFromPlacedBlocksMap(blockState, event.getBlock().getLocation());
                    } else {
                        event.setCancelled(true);
                    }
                    return;
                }

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingSpleefImpl.class)) {
                    Block block = event.getBlock();
                    if (block.getType() != Material.SNOW_BLOCK) {
                        event.setCancelled(true);
                        return;
                    }

                    profile.getMatch().addBlockToBrokenBlocksMap(block.getState(), block.getLocation());
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
        Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());
        switch (profile.getState()) {
            case PLAYING:
                if (profile.getMatch().getState() == EnumMatchState.STARTING) event.setCancelled(true);
                if (profile.getMatch().getState() == EnumMatchState.ENDING_MATCH) event.setCancelled(true);

                if (profile.getMatch().getKit().isSettingEnabled(KitSettingBuildImpl.class)) {
                    profile.getMatch().addBlockToPlacedBlocksMap(event.getBlock().getState(), event.getBlockPlaced().getLocation());
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
            Profile profile = this.plugin.getProfileRepository().getProfile(player.getUniqueId());

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
}