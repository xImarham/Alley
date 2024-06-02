package me.emmy.alley.profile.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.division.AbstractDivision;
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
public class ProfileMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Profile Menu";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        AbstractDivision abstractDivision = Alley.getInstance().getDivisionRepository().getDivision(profile.getProfileData().getProfileDivisionData().getDivision());

        buttons.put(10, new ProfileButton("&d&lYour Statistics", Material.PAPER, (short) 0, Arrays.asList(
                "",
                "&d&lGlobal",
                " &d● &fWins: &dnull",
                " &d● &fLosses: &dnull",
                " &d● &fElo: &d" + profile.getProfileData().getProfileDivisionData().getGlobalElo(),
                "",
                "&d&lRanked",
                " &d● &fWins: &d" + profile.getProfileData().getRankedWins(),
                " &d● &fLosses: &d" + profile.getProfileData().getRankedLosses(),
                "",
                "&d&lUnranked",
                " &d● &fWins: &d" + profile.getProfileData().getUnrankedWins(),
                " &d● &fLosses: &d" + profile.getProfileData().getUnrankedLosses(),
                "",
                "&aClick to view!"
        )));

        buttons.put(11, new ProfileButton("&d&lMatch History", Material.BOOK, (short) 0, Arrays.asList(
                "",
                "&fView your match history.",
                "",
                "&aClick to view!"
        )));

        buttons.put(12, new ProfileButton("&d&lThemes", Material.ENDER_CHEST, (short) 0, Arrays.asList(
                "",
                "&fCustomize your profile theme.",
                "",
                "&aClick to view!"
        )));

        buttons.put(4, new ProfileButton("&d&lYour Profile", Material.SKULL_ITEM, (short) 3, Arrays.asList(
                "",
                " &d● &fCoins: &d" + profile.getProfileData().getCoins(),
                " &d● &fLevel: &dnull",
                "",
                " &d● &fNext level: &dnull",
                " &d● &fProgress: &dnull%"
        )));

        buttons.put(14, new ProfileButton("&d&lDivisions", Material.FEATHER, (short) 0, Arrays.asList(
                "",
                " &d● &fCurrent Division: &d" + abstractDivision.getTier().getName(),
                " &d● &fNext Division: &dnull",
                "",
                " &d● &fCurrent level: &d" + abstractDivision.getLevel().getName(),
                "",
                "&aClick to view all divisions!"
        )));

        buttons.put(15, new ProfileButton("&d&lProfile Settings", Material.ANVIL, (short) 0, Arrays.asList(
                "",
                "&fCustomize your profile settings",
                "&fto your preference.",
                "",
                "&aClick to view!"
        )));

        buttons.put(16, new ProfileButton("&d&lCosmetics", Material.BEACON, (short) 0, Arrays.asList(
                "",
                "&fCustomize your available",
                "&fcosmetics.",
                "",
                "&aClick to view!"
        )));

        buttons.put(20, new ProfileButton("&d&lLeaderboards", Material.EYE_OF_ENDER, (short) 0, Arrays.asList(
                "",
                "&fView the leaderboards",
                "&fwhere the best players",
                "&fhave taken their place.",
                "",
                " &d&lTop Elo:",
                "  &d1. &fnull",
                "  &d2. &fnull",
                "  &d3. &fnull",
                "",
                " &d&lTop 3 Unranked Wins:",
                "  &d1. &fnull",
                "  &d2. &fnull",
                "  &d3. &fnull",
                "",
                " &d&lTop 3 Ranked Wins:",
                "  &d1. &fnull",
                "  &d2. &fnull",
                "  &d3. &fnull",
                "",
                "&aClick to see the leaderboards!"
        )));

        buttons.put(25, new ProfileButton("&d&lCoin Shop", Material.EMERALD, (short) 0, Arrays.asList(
                "",
                "&fPurchase cosmetics,",
                "&fthemes, and more with",
                "&fthe coins you earn.",
                "",
                "&aClick to view!"
        )));

        addBorder(buttons, (byte) 6, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}

