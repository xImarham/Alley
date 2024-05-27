package me.emmy.alley.hotbar.listener;

import me.emmy.alley.Alley;
import me.emmy.alley.hotbar.HotbarItem;
import me.emmy.alley.leaderboard.menu.personal.StatisticsMenu;
import me.emmy.alley.match.menu.CurrentMatchesMenu;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.settings.menu.SettingsMenu;
import me.emmy.alley.queue.menu.queues.QueuesMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HotbarListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if ((action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) {
            return;
        }

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        HotbarItem hotbarItem = Alley.getInstance().getHotbarRepository().getItemByStack(item);

        if (hotbarItem != null) {
            String command = hotbarItem.getHotbarItems().getCommand();
            if (command != null && !command.isEmpty()) {
                player.performCommand(command);
            } else {
                switch (profile.getState()) {
                    case LOBBY:
                        switch (hotbarItem.getHotbarItems()) {
                            case QUEUES:
                                new QueuesMenu().openMenu(player);
                                break;
                            case CURRENT_MATCHES:
                                new CurrentMatchesMenu().openMenu(player);
                                break;
                            case KIT_EDITOR:
                                break;
                            case LEADERBOARD:
                                new StatisticsMenu().openMenu(player);
                                break;
                            case SETTINGS:
                                new SettingsMenu().openMenu(player);
                                break;
                        }
                        break;
                }
            }
        }
    }
}
