package dev.revere.alley.profile.settings.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileSettingData;
import dev.revere.alley.profile.settings.menu.button.PracticeSettingsButton;
import lombok.AllArgsConstructor;
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
public class PracticeSettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "&b&lPractice Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        //buttons.put(0, new BackButton(new ProfileMenu()));

        buttons.put(10, new PracticeSettingsButton("&b&lToggle Party Messages", Material.FEATHER, 0, Arrays.asList(
                "&fDecide whether you should",
                "&fsee the &bparty chat",
                "&fmessages or not.",
                "",
                " &f● &bStatus: &7" + (profile.getProfileData().getProfileSettingData().isPartyMessagesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new PracticeSettingsButton("&b&lToggle Party Invites", Material.NAME_TAG, 0, Arrays.asList(
                "&fDecide whether you should",
                "&freceive &bparty invites",
                "&for not.",
                "",
                " &f● &bStatus: &7" + (profile.getProfileData().getProfileSettingData().isPartyInvitesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(12, new PracticeSettingsButton("&b&lToggle Scoreboard", Material.CARPET, 5, Arrays.asList(
                "&fDecide whether you should",
                "&fsee the &bscoreboard",
                "&for not.",
                "",
                " &f● &bStatus: &7" + (profile.getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(13, new PracticeSettingsButton("&b&lToggle Tablist", Material.ITEM_FRAME, 0, Arrays.asList(
                "&fDecide whether you should",
                "&fsee the &btablist",
                "&for not.",
                "",
                " &f● &bStatus: &7" + (profile.getProfileData().getProfileSettingData().isTablistEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        ProfileSettingData profileSettingData = profile.getProfileData().getProfileSettingData();
        buttons.put(14, new PracticeSettingsButton("&b&lWorld time", Material.WATCH, 0, Arrays.asList(
                "&fChange your world time",
                "&fto &bday&f, &bnight&f, or &bsunset&f.",
                "",
                profileSettingData.isDefaultTime() ? " &b● &a&lDefault" : " &b● &7Default",
                profileSettingData.isDayTime() ? " &b● &e&lDay" : " &b● &7Day",
                profileSettingData.isSunsetTime() ? " &b● &6&lSunset" : " &b● &7Sunset",
                profileSettingData.isNightTime() ? " &b● &4&lNight" : " &b● &7Night",
                "",
                "&aClick to change!"
        )));

        buttons.put(16, new PracticeSettingsButton("&b&lMatch Settings", Material.BOOK, 0, Arrays.asList(
                "&fAdjust your match settings.",
                "",
                "&aClick to view!"
        )));

        buttons.put(19, new PracticeSettingsButton("&b&lShow Scoreboard Lines", Material.STRING, 0, Arrays.asList(
                "&fDecide whether you should",
                "&fsee the &bscoreboard lines",
                "&for not.",
                "",
                " &f● &bStatus: &7" + (profile.getProfileData().getProfileSettingData().isShowScoreboardLines() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(25, new PracticeSettingsButton("&b&lCosmetics", Material.NETHER_STAR, 0, Arrays.asList(
                "&fCustomize your cosmetics.",
                "",
                "&aClick to view!"
        )));

        this.addBorder(buttons, (byte) 15, 4);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 4;
    }
}