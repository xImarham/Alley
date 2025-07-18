package dev.revere.alley.feature.item.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.item.IItemService;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Emmy
 * @project alley-practice
 * @since 18/07/2025
 */
public class ItemListener implements Listener {

    @EventHandler
    private void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());
        if (profile.getMatch() == null && profile.getFfaMatch() == null) {
            return;
        }

        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        IItemService itemService = Alley.getInstance().getService(IItemService.class);
        if (item.isSimilar(itemService.getGoldenHead())) {
            event.setCancelled(true);
            itemService.performHeadConsume(player, item);
        }
    }
}