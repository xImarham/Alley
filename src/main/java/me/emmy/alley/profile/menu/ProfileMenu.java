package me.emmy.alley.profile.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.ProfileKitData;
import me.emmy.alley.profile.division.AbstractDivision;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:27
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

        buttons.put(10, new ProfileButton("&b&lYour Statistics", new ItemStack(Material.PAPER), Arrays.asList(
                "",
                "&b&lGlobal",
                " &b● &fWins: &b" + profile.getProfileData().getTotalWins(),
                " &b● &fLosses: &b" + profile.getProfileData().getTotalLosses(),
                " &b● &fElo: &b" + profile.getProfileData().getProfileDivisionData().getGlobalElo(),
                "",
                "&b&lRanked",
                " &b● &fWins: &b" + profile.getProfileData().getRankedWins(),
                " &b● &fLosses: &b" + profile.getProfileData().getRankedLosses(),
                "",
                "&b&lUnranked",
                " &b● &fWins: &b" + profile.getProfileData().getUnrankedWins(),
                " &b● &fLosses: &b" + profile.getProfileData().getUnrankedLosses(),
                "",
                "&aClick to view!"
        )));

        buttons.put(11, new ProfileButton("&b&lMatch History", new ItemStack(Material.BOOK), Arrays.asList(
                "",
                "&fView your match history.",
                "",
                "&aClick to view!"
        )));

        buttons.put(12, new ProfileButton("&b&lThemes", new ItemStack(Material.ENDER_CHEST), Arrays.asList(
                "",
                "&fCustomize your profile theme.",
                "",
                "&aClick to view!"
        )));

        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setOwner(player.getName());
            skullItem.setItemMeta(skullMeta);
        }

        buttons.put(4, new ProfileButton("&b&lYour Profile", skullItem, Arrays.asList(
                "",
                " &b● &fCoins: &b" + profile.getProfileData().getCoins(),
                " &b● &fLevel: &bnull",
                "",
                " &b● &fNext level: &bnull",
                " &b● &fProgress: &bnull%"
        )));

        /*String[] nextDivisionAndLevel = abstractDivision.getNextDivisionAndLevelArray();
        int eloNeeded = abstractDivision.getEloNeededForDivision(EnumDivisionTier.valueOf(nextDivisionAndLevel[0].toUpperCase()), EnumDivisionLevel.valueOf("LEVEL_" + nextDivisionAndLevel[1]));
        String progressBar = abstractDivision.generateProgressBar(eloNeeded);*/
        buttons.put(14, new ProfileButton("&b&lDivisions", new ItemStack(Material.FEATHER), Arrays.asList(
                "",
                //" &b● &fCurrent Division: &b" + abstractDivision.getTier().getName(),
                //" &b● &fCurrent Level: &b" + abstractDivision.getLevel().getName(),
                "",
                //" &b● &fNext Division: &b" + abstractDivision.getNextDivisionAndLevel(),
                " &b● &fProgress: &b" + "null",
                "",
                "&aClick to view all divisions!"
        )));

        buttons.put(15, new ProfileButton("&b&lProfile Settings", new ItemStack(Material.ANVIL), Arrays.asList(
                "",
                "&fCustomize your profile settings",
                "&fto your preference.",
                "",
                "&aClick to view!"
        )));

        buttons.put(16, new ProfileButton("&b&lChallenges", new ItemStack(Material.BEACON), Arrays.asList(
                "",
                "&fView your current challenges",
                "&fand complete them for rewards.",
                "",
                "&aClick to view!"
        )));

        buttons.put(20, new ProfileButton("&b&lLeaderboards", new ItemStack(Material.EYE_OF_ENDER), Arrays.asList(
                "",
                "&fView the leaderboards",
                "&fwhere the best players",
                "&fhave taken their place.",
                "",
                " &b&lTop Elo:",
                "  &b1. &fnull",
                "  &b2. &fnull",
                "  &b3. &fnull",
                "",
                " &b&lTop 3 Unranked Wins:",
                "  &b1. &fnull",
                "  &b2. &fnull",
                "  &b3. &fnull",
                "",
                " &b&lTop 3 Ranked Wins:",
                "  &b1. &fnull",
                "  &b2. &fnull",
                "  &b3. &fnull",
                "",
                "&aClick to see the leaderboards!"
        )));

        buttons.put(24, new ProfileButton("&b&lCoin Shop", new ItemStack(Material.EMERALD), Arrays.asList(
                "",
                "&fPurchase cosmetics,",
                "&fthemes, and more with",
                "&fthe coins you earn.",
                "",
                "&aClick to view!"
        )));

        addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 5;
    }
}
