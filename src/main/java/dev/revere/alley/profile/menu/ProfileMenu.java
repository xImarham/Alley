package dev.revere.alley.profile.menu;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.stats.menu.StatisticsMenu;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.division.AbstractDivision;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.api.menu.pagination.ItemBuilder;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
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

        ItemStack skullItem = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();

        if (skullMeta != null) {
            skullMeta.setOwner(player.getName());
            skullItem.setItemMeta(skullMeta);
        }

        String[] nextDivisionAndLevel = abstractDivision.getNextDivisionAndLevelArray();
        int eloNeeded = abstractDivision.getEloNeededForDivision(EnumDivisionTier.valueOf(nextDivisionAndLevel[0].toUpperCase()), EnumDivisionLevel.valueOf("LEVEL_" + nextDivisionAndLevel[1]));

        buttons.put(4, new ProfileButton("&b&lYour Profile", skullItem, Arrays.asList(
                " &b&l● &fCoins: &b" + profile.getProfileData().getCoins(),
                " &b&l● &fDivision: &b" + abstractDivision.getTier().getName(),
                " &b&l● &fLevel: &b" + abstractDivision.getLevel().getLevelInt(),
                " &b&l● &fProgress: &b" + abstractDivision.generateProgressBar(eloNeeded),
                "",
                " &b&l● &fNext Division: &b" + abstractDivision.getNextDivisionAndLevel(),
                ""
        )));

        buttons.put(10, new ProfileButton("&b&lYour Statistics", new ItemStack(Material.PAPER), Arrays.asList(
                "",
                "&b&lGlobal",
                " &b&l● &fWins: &b" + profile.getProfileData().getTotalWins(),
                " &b&l● &fLosses: &b" + profile.getProfileData().getTotalLosses(),
                " &b&l● &fElo: &b" + profile.getProfileData().getProfileDivisionData().getGlobalElo(),
                "",
                "&b&lRanked",
                " &b&l● &fWins: &b" + profile.getProfileData().getRankedWins(),
                " &b&l● &fLosses: &b" + profile.getProfileData().getRankedLosses(),
                "",
                "&b&lUnranked",
                " &b&l● &fWins: &b" + profile.getProfileData().getUnrankedWins(),
                " &b&l● &fLosses: &b" + profile.getProfileData().getUnrankedLosses(),
                "",
                "&b&lFFA",
                " &b&l● &fKills: &b" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getKills).sum(),
                " &b&l● &fDeaths: &b" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getDeaths).sum(),
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
                    new StatisticsMenu(player).openMenu(player);
                    break;
                case SKULL_ITEM:
                    break;
            }

            playNeutral(player);
        }
    }
}
