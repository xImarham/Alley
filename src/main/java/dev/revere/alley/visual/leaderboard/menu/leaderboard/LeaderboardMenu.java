package dev.revere.alley.visual.leaderboard.menu.leaderboard;

import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.visual.leaderboard.menu.button.StatisticsButton;
import dev.revere.alley.visual.leaderboard.menu.leaderboard.button.DisplayTypeButton;
import dev.revere.alley.visual.leaderboard.menu.leaderboard.button.KitButton;
import dev.revere.alley.visual.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.ProfileMenu;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.button.BackButton;
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
