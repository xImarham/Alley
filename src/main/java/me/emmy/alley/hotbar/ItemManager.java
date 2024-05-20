package me.emmy.alley.hotbar;

import me.emmy.alley.Alley;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ItemManager implements Listener {

    public ItemManager() {
        Alley.getInstance().getServer().getPluginManager().registerEvents(this, Alley.getInstance());
    }

    public void applySpawnItems(Player player) {
        player.getInventory().setItem(LobbyItem.UNRANKED_QUEUE.getSlot(), LobbyItem.UNRANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.RANKED_QUEUE.getSlot(), LobbyItem.RANKED_QUEUE.constructItem());
        player.getInventory().setItem(LobbyItem.LEADERBOARD.getSlot(), LobbyItem.LEADERBOARD.constructItem());
        player.getInventory().setItem(LobbyItem.EVENTS.getSlot(), LobbyItem.EVENTS.constructItem());
        player.getInventory().setItem(LobbyItem.SETTINGS.getSlot(), LobbyItem.SETTINGS.constructItem());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null)
            return;

        LobbyItem clickedItem = LobbyItem.getByItemStack(event.getItem());

        if (clickedItem == null)
            return;

        if (clickedItem.getCommand() != null) {
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            player.performCommand(clickedItem.getCommand());
        } else {
            int menuID = clickedItem.getMenu();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.getInventory().clear();
        applySpawnItems(player);
    }
}
