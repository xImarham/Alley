package me.emmy.alley.profile.settings.matchsettings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.profile.settings.playersettings.menu.SettingsMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.button.BackButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class MatchSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Match Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new SettingsMenu()));

        buttons.put(10, new MatchSettingsButton("&d&lClear inventory", Material.CHEST, (short) 0, Arrays.asList(
                "",
                "&fClear your inventory",
                "&fafter every &dkill &fin",
                "&fa duel.",
                "",
                "&fStatus: &4null",
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new MatchSettingsButton("&d&lToggle Flight", Material.FEATHER, (short) 0, Arrays.asList(
                "",
                "&fStart flying upon ",
                "&fyour opponents &ddeath",
                "&fin a duel.",
                "",
                "&fStatus: &4null",
                "",
                "&aClick to toggle!"
        )));

        buttons.put(31, new MatchSettingsButton("&d&lCosmetics", Material.FIREWORK, (short) 0, Arrays.asList(
                "",
                "&fManage your &dcosmetics",
                "&fand &dkill effects.",
                "",
                "&aClick to manage!"
        )));

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}

