package me.emmy.alley.profile.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.leaderboard.menu.personal.StatisticsMenu;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.division.AbstractDivision;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import me.emmy.alley.profile.division.menu.DivisionsMenu;
import me.emmy.alley.profile.settings.playersettings.menu.SettingsMenu;
import me.emmy.alley.utils.menu.Button;
import me.emmy.alley.utils.menu.Menu;
import me.emmy.alley.utils.menu.pagination.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
                " &b● &fLevel: &b" + abstractDivision.getLevel().getName(),
                "",
                " &b● &fNext level: &b" + abstractDivision.getNextDivisionAndLevel(),
                " &b● &fProgress: &b" + "null"
        )));

        buttons.put(13, new ProfileButton("&b&lChallenges", new ItemStack(Material.BEACON), Arrays.asList(
                "",
                "&fView your current challenges",
                "&fand complete them for rewards.",
                "",
                "&aClick to view!"
        )));

        String[] nextDivisionAndLevel = abstractDivision.getNextDivisionAndLevelArray();
        int eloNeeded = abstractDivision.getEloNeededForDivision(EnumDivisionTier.valueOf(nextDivisionAndLevel[0].toUpperCase()), EnumDivisionLevel.valueOf("LEVEL_" + nextDivisionAndLevel[1]));
        String progressBar = abstractDivision.generateProgressBar(eloNeeded);
        buttons.put(14, new ProfileButton("&b&lDivisions", new ItemStack(Material.FEATHER), Arrays.asList(
                "",
                " &b● &fCurrent Division: &b" + abstractDivision.getTier().getName(),
                " &b● &fCurrent Level: &b" + abstractDivision.getLevel().getName(),
                "",
                " &b● &fNext Division: &b" + abstractDivision.getNextDivisionAndLevel(),
                " &b● &fProgress: &b" + progressBar,
                "",
                "&aClick to view all divisions!"
        )));

        buttons.put(15, new ProfileButton("&c&lEmpty", new ItemStack(Material.REDSTONE_BLOCK), Arrays.asList(
                "",
                "&fThis slot is empty.",
                "",
                "&aClick for nothing to happen!"
        )));

        buttons.put(16, new ProfileButton("&b&lProfile Settings", new ItemStack(Material.ANVIL), Arrays.asList(
                "",
                "&fCustomize your profile settings",
                "&fto your preference.",
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

    @AllArgsConstructor
    public static class ProfileButton extends Button {

        private String displayName;
        private ItemStack itemStack;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(itemStack)
                    .name(displayName)
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
                return;
            }

            Material material = itemStack.getType();

            switch (material) {
                case PAPER:
                    new StatisticsMenu().openMenu(player);
                    break;
                case BOOK:
                    player.performCommand("matchhistory");
                    break;
                case SKULL_ITEM:
                    break;
                case ANVIL:
                    new SettingsMenu().openMenu(player);
                    break;
                case FEATHER:
                    new DivisionsMenu().openMenu(player);
                    break;
                case BEACON:
                    player.performCommand("challenges");
                    break;
                case ENDER_CHEST:
                    player.performCommand("themes");
                    break;
            }
            playNeutral(player);
        }
    }
}
