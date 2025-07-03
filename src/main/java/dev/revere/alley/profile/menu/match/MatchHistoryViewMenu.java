package dev.revere.alley.profile.menu.match;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.menu.match.button.MatchHistoryViewButton;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
@AllArgsConstructor
public class MatchHistoryViewMenu extends Menu {
    protected final Kit kit;

    @Override
    public String getTitle(Player player) {
        return "";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());

        List<AbstractMatchData> matchDataList = profile.getProfileData().getPreviousMatches();

        List<AbstractMatchData> filteredMatches = matchDataList.stream()
                .filter(matchData -> matchData.getKit().equals(this.kit.getName()))
                .collect(Collectors.toList());

        filteredMatches.forEach(
                matchData -> buttons.put(buttons.size(), new MatchHistoryViewButton(matchData))
        );

        return buttons;
    }
}