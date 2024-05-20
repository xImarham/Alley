package me.emmy.alley.arena.listener;

import me.emmy.alley.arena.selection.Selection;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Difficulty;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
public class ArenaListener implements Listener {

    @EventHandler
    private void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (!(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        ItemStack itemStack = event.getItem();
        if (itemStack == null || !itemStack.equals(Selection.SELECTION_TOOL)) {
            return;
        }

        Player player = event.getPlayer();
        Block clickedBlock = event.getClickedBlock();
        int locationType;

        Selection selection = Selection.createSelection(player);
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            selection.setMinimum(clickedBlock.getLocation());
            locationType = 2;
        } else {
            selection.setMaximum(clickedBlock.getLocation());
            locationType = 1;
        }

        event.setCancelled(true);
        event.setUseItemInHand(PlayerInteractEvent.Result.DENY);
        event.setUseInteractedBlock(PlayerInteractEvent.Result.DENY);

        String message = locationType == 1 ? "Maximum | " + clickedBlock.getLocation() : "Minimum | " + clickedBlock.getLocation() + " | location has been set!";
        player.sendMessage(CC.translate(message));
    }

    @EventHandler
    private void onUnlockChunk(ChunkUnloadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onPrime(ExplosionPrimeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    private void onWorldLoad(WorldLoadEvent event) {
        event.getWorld().getEntities().clear();
        event.getWorld().setDifficulty(Difficulty.HARD);
    }
}
