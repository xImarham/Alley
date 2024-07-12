package me.emmy.alley.profile.settings.playersettings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.menu.ProfileMenu;
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
public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(0, new BackButton(new ProfileMenu()));

        buttons.put(10, new SettingsButton("&d&lToggle Party Messages", Material.FEATHER, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &dparty chat",
                "&fmessages or not.",
                "",
                " &d● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyMessagesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new SettingsButton("&d&lToggle Party Invites", Material.NAME_TAG, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&freceive &dparty invites",
                "&for not.",
                "",
                " &d● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isPartyInvitesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(12, new SettingsButton("&d&lToggle Scoreboard", Material.CARPET, (short) 5, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &dscoreboard",
                "&for not.",
                "",
                " &d● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(13, new SettingsButton("&d&lToggle Tablist", Material.ITEM_FRAME, (short) 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &dtablist",
                "&for not.",
                "",
                " &d● &fStatus: &r" + (Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId()).getProfileData().getProfileSettingData().isTablistEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(14, new SettingsButton("&d&lWorld time", Material.WATCH, (short) 0, Arrays.asList(
                "",
                "&fChange your world time",
                "&fto &dday&f, &dnight&f, or &dsunset&f.",
                "",
                " &d● &fStatus: &rnull",
                "",
                "&aClick to change!"
        )));

        buttons.put(16, new SettingsButton("&d&lMatch Settings", Material.BOOK_AND_QUILL, (short) 0, Arrays.asList(
                "",
                "&fChange your match settings",
                "&fsuch as &dmatch sounds&f, &dmatch particles&f,",
                "&dmatch cosmetics&f, and &dmatch animations&f.",
                "",
                "&aClick to view!"
        )));

        addBorder(buttons, (byte) 6, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }
}

