package me.emmy.alley.leaderboard.menu.personal;

import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.leaderboard.menu.button.StatisticsButton;
import me.emmy.alley.leaderboard.menu.personal.button.GlobalStatButton;
import me.emmy.alley.leaderboard.menu.personal.button.KitStatButton;
import me.emmy.alley.leaderboard.menu.personal.button.LeaderboardButton;
import me.emmy.alley.profile.menu.ProfileMenu;
import me.emmy.alley.queue.menu.queues.QueuesMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class StatisticsMenu extends Menu {
    /**
     * Gets the title of the menu.
     *
     * @param player the player viewing the menu
     * @return the title of the menu
     */
    @Override
    public String getTitle(Player player) {
        return "&8Personal Statistics";
    }

    /**
     * Gets the buttons to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the buttons to display
     */
    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new ProfileMenu()));
        buttons.put(2, new StatisticsButton());
        buttons.put(4, new GlobalStatButton());
        buttons.put(6, new LeaderboardButton());

        Alley.getInstance().getKitRepository().getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> kit.getIcon() != null)
                .forEach(kit -> buttons.put(kit.getUnrankedslot(), new KitStatButton(kit)));

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
