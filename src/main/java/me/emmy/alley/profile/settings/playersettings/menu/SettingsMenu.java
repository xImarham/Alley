package me.emmy.alley.profile.settings.playersettings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.menu.ProfileMenu;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.button.BackButton;
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
public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new ProfileMenu()));

        buttons.put(10, new SettingsButton("&b&lToggle Party Messages", Material.FEATHER, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &bparty chat",
                "&fmessages or not.",
                "",
                " &b● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyMessagesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new SettingsButton("&b&lToggle Party Invites", Material.NAME_TAG, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&freceive &bparty invites",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyInvitesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(12, new SettingsButton("&b&lToggle Scoreboard", Material.CARPET, (short) 5, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &bscoreboard",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(13, new SettingsButton("&b&lToggle Tablist", Material.ITEM_FRAME, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &btablist",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isTablistEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(14, new SettingsButton("&b&lWorld time", Material.WATCH, (short) 0, Arrays.asList(
                "",
                "&fChange your world time",
                "&fto &bday&f, &bnight&f, or &bsunset&f.",
                "",
                " &b● &fStatus: &rnull",
                "",
                "&aClick to change!"
        )));

        buttons.put(16, new SettingsButton("&b&lMatch Settings", Material.BOOK_AND_QUILL, (short) 0, Arrays.asList(
                "",
                "&fChange your match settings",
                "&fsuch as &bmatch sounds&f, &bmatch particles&f,",
                "&bmatch cosmetics&f, and &bmatch animations&f.",
                "",
                "&aClick to view!"
        )));

        addBorder(buttons, (byte) 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

