package dev.revere.alley.profile.menu.match;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.visibility.IVisibilityService;
import dev.revere.alley.profile.menu.match.button.MatchHistorySelectKitButton;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @since 04/06/2025
 */
public class MatchHistorySelectKitMenu extends Menu {
    @Override
    public String getTitle(Player player) {
        return "&6&lMatch History &f- &7Select Kit";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Alley.getInstance().getService(IKitService.class).getKits().forEach(
                kit -> buttons.put(buttons.size(), new MatchHistorySelectKitButton(kit))
        );

        this.addGlass(buttons, 15);

        return buttons;
    }
}
