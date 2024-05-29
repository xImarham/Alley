package me.emmy.alley.profile.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 23/05/2024 - 01:27
 */

@AllArgsConstructor
public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new SettingsButton("&dToggle Party Messages", Material.FEATHER, (short) 0, Arrays.asList(
                "",
                "&7Decide whether you should",
                "&7see the party chat",
                "&7messages or not.",
                "",
                "&7Status: " + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyMessagesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&fClick to toggle!"
        )));

        buttons.put(12, new SettingsButton("&dToggle Party Invites", Material.NAME_TAG, (short) 0, Arrays.asList(
                "",
                "&7Decide whether you should",
                "&7receive party invites",
                "&7or not.",
                "",
                "&7Status: " + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyInvitesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&fClick to toggle!"
        )));

        buttons.put(13, new SettingsButton("&dToggle Scoreboard", Material.CARPET, (short) 5, Arrays.asList(
                "",
                "&7Decide whether you should",
                "&7see the scoreboard",
                "&7or not.",
                "",
                "&7Status: " + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&fClick to toggle!"
        )));

        buttons.put(14, new SettingsButton("&dToggle Tablist", Material.ITEM_FRAME, (short) 0, Arrays.asList(
                "",
                "&7Decide whether you should",
                "&7see the tablist",
                "&7or not.",
                "",
                "&7Status: " + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isTablistEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&fClick to toggle!"
        )));

        for (int slot = 0; slot < getSize(); slot++) {
            if (!buttons.containsKey(slot)) {
                buttons.put(slot, new SettingsButton("", Material.STAINED_GLASS_PANE, (short) 6, Arrays.asList(
                        "",
                        ""
                )));
            }
        }

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

