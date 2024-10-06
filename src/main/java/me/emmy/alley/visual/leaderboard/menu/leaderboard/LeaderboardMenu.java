package me.emmy.alley.visual.leaderboard.menu.leaderboard;

import com.google.common.collect.Maps;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.visual.leaderboard.menu.button.StatisticsButton;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.button.DisplayTypeButton;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.button.KitButton;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.menu.ProfileMenu;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.button.BackButton;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 14:51
 */

public class LeaderboardMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&8Leaderboard";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = Maps.newHashMap();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        EnumLeaderboardType currentType = profile.getLeaderboardType();

        buttons.put(0, new BackButton(new ProfileMenu()));
        buttons.put(2, new StatisticsButton());
        buttons.put(6, new DisplayTypeButton());

        Alley.getInstance().getKitRepository().getKits().stream()
                .filter(Kit::isEnabled)
                .filter(kit -> kit.getIcon() != null)
                .forEach(kit -> {
                    if (currentType == EnumLeaderboardType.RANKED) {
                        if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
                            buttons.put(kit.getRankedslot(), new KitButton(currentType, kit));
                        }
                    } else {
                        buttons.put(kit.getUnrankedslot(), new KitButton(currentType, kit));
                    }
                });

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }
}
