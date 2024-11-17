package dev.revere.alley.stats.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import dev.revere.alley.profile.data.impl.ProfileUnrankedKitData;
import dev.revere.alley.stats.menu.button.GlobalStatButton;
import dev.revere.alley.stats.menu.button.LeaderboardButton;
import dev.revere.alley.stats.menu.button.StatisticsButton;
import dev.revere.alley.util.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

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
        return "&b&lYour Stats";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(2, new StatisticsButton());
        buttons.put(4, new GlobalStatButton());
        buttons.put(6, new LeaderboardButton());

        List<Map.Entry<String, ProfileRankedKitData>> sortedKits = Alley.getInstance().getProfileRepository().getProfile(target.getUniqueId()).getProfileData().getRankedKitData().entrySet().stream()
                .filter(entry -> entry.getValue().getWins() != 0 || entry.getValue().getLosses() != 0)
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().getWins(), entry1.getValue().getWins()))
                .collect(Collectors.toList());

        int slot = 10;

        // Loop through the sorted kits and add a button for each kit that the player has played
        for (Map.Entry<String, ProfileRankedKitData> entry : sortedKits) {
            buttons.put(slot++, new KitStatButton(Alley.getInstance().getKitRepository().getKit(entry.getKey())));
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
        private final Kit kit;

        @Override
        public ItemStack getButtonItem(Player player) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
            ProfileRankedKitData profileRankedKitData = profile.getProfileData().getRankedKitData().get(this.kit.getName());
            ProfileUnrankedKitData profileUnrankedKitData = profile.getProfileData().getUnrankedKitData().get(this.kit.getName());
            ProfileFFAData profileFFAData = profile.getProfileData().getFfaData().get(this.kit.getName());

            List<String> lore = new ArrayList<>(Arrays.asList(
                    "",
                    "&b&lUnranked",
                    "&f● &bWins: &f" + profileUnrankedKitData.getWins(),
                    "&f● &bLosses: &f" + profileUnrankedKitData.getLosses(),
                    "",
                    "&b&lRanked",
                    "&f● &bWins: &f" + profileRankedKitData.getWins(),
                    "&f● &bLosses: &f" + profileRankedKitData.getLosses(),
                    "&f● &bElo: &f" + profileRankedKitData.getElo(),
                    ""
            ));

            if (this.kit.isFfaKit()) {
                lore.addAll(Arrays.asList(
                        "&b&lFFA",
                        "&f● &bKills: &f" + profileFFAData.getKills(),
                        "&f● &bDeaths: &f" + profileFFAData.getDeaths(),
                        "&f● &bKDR: &f" + profileFFAData.getKdr(),
                        ""
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