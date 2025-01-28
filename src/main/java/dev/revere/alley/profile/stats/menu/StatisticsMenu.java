package dev.revere.alley.profile.stats.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import dev.revere.alley.profile.data.impl.ProfileUnrankedKitData;
import dev.revere.alley.profile.stats.menu.button.GlobalStatButton;
import dev.revere.alley.profile.stats.menu.button.LeaderboardButton;
import dev.revere.alley.profile.stats.menu.button.StatisticsButton;
import dev.revere.alley.util.data.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 12:25
 */
@AllArgsConstructor
public class StatisticsMenu extends Menu {
    private OfflinePlayer target;

    @Override
    public String getTitle(Player player) {
        return this.target == player ? "&b&lYour Stats" : "&b&l" + this.target.getName() + "'s Stats";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new StatisticsButton());
        buttons.put(4, new GlobalStatButton());
        buttons.put(6, new LeaderboardButton());

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(this.target == player ? player.getUniqueId() : this.target.getUniqueId());
        List<Kit> sortedKits = profile.getParticipatedKits();

        int slot = 10;
        for (Kit kit : sortedKits) {
            buttons.put(slot++, new KitStatButton(profile, kit));
            if (slot == 17 || slot == 26 || slot == 35 || slot == 44 || slot == 53) {
                slot += 2;
            }
        }

        this.addBorder(buttons, (byte) 15, 5);

        return buttons;
    }

    @Override
    public int getSize() {
        return 5 * 9;
    }

    @AllArgsConstructor
    private static class KitStatButton extends Button {
        private final Profile profile;
        private final Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            ProfileRankedKitData profileRankedKitData = this.profile.getProfileData().getRankedKitData().get(this.kit.getName());
            ProfileUnrankedKitData profileUnrankedKitData = this.profile.getProfileData().getUnrankedKitData().get(this.kit.getName());
            ProfileFFAData profileFFAData = this.profile.getProfileData().getFfaData().get(this.kit.getName());

            List<String> lore = new ArrayList<>(Arrays.asList(
                    "&b&lUnranked &6⭐" + profileUnrankedKitData.getDivision().getName() + " " + profileUnrankedKitData.getTier().getName(),
                    "&f● &bWins: &f" + profileUnrankedKitData.getWins(),
                    //"&f● &bLosses: &f" + profileUnrankedKitData.getLosses(),
                    "",
                    "&f● &bWin Streak: " + "&fN/A",
                    "    &bBest: " + "&fN/A" + " &7(N/A Daily)"
            ));

            if (this.profile.hasParticipatedInRanked()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&b&lRanked",
                        "&f● &bWins: &f" + profileRankedKitData.getWins(),
                        //"&f● &bLosses: &f" + profileRankedKitData.getLosses(),
                        "&f● &bElo: &f" + profileRankedKitData.getElo()
                ));
            }

            if (this.profile.hasParticipatedInTournament()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&b&lTournament",
                        "&f● &bWins: &f" + "N/A",
                        "&f● &bLosses: &f" + "N/A"
                ));
            }

            if (this.kit.isFfaKit() && this.profile.hasParticipatedInFFA()) {
                lore.addAll(Arrays.asList(
                        "",
                        "&b&lFFA",
                        "&f● &bKills: &f" + profileFFAData.getKills() + " &7(" + profileFFAData.getKdr() + "x)",
                        "&f● &bDeaths: &f" + profileFFAData.getDeaths()
                ));
            }

            return new ItemBuilder(this.kit.getIcon())
                    .name("&b&l" + this.kit.getDisplayName())
                    .durability(this.kit.getIconData())
                    .lore(lore)
                    .hideMeta()
                    .build();
        }
    }
}