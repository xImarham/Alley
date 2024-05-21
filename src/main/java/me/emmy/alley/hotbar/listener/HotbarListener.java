package me.emmy.alley.hotbar.listener;

import me.emmy.alley.hotbar.LobbyItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class HotbarListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getItem() == null) {
            return;
        }

        LobbyItem clickedItem = LobbyItem.getByItemStack(event.getItem());
        if (clickedItem == null) {
            return;
        }

        if (clickedItem.getCommand() != null) {
            if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            player.performCommand(clickedItem.getCommand());
        } else {
            int menuID = clickedItem.getMenu();
            // TODO: Open the menu
        }
    }
}
