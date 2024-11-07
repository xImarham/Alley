package dev.revere.alley.visual.leaderboard.menu.leaderboard.button;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.visual.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.util.item.ItemBuilder;
import dev.revere.alley.api.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class KitButton extends Button {
    private final EnumLeaderboardType leaderboardType;
    private final Kit kit;

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        List<String> lore = new ArrayList<>();

        lore.add("");
        switch (leaderboardType) {
            case RANKED:
                lore.add("&f● &bWins: &f" + profile.getProfileData().getRankedWins());
                lore.add("&f● &bLosses: &f" + profile.getProfileData().getRankedLosses());
                lore.add("&f● &bElo: &f" + profile.getProfileData().getProfileDivisionData().getGlobalElo());
                break;
            case UNRANKED:
                lore.add("&f● &bWins: &f" + profile.getProfileData().getUnrankedWins());
                lore.add("&f● &bLosses: &f" + profile.getProfileData().getUnrankedLosses());
                break;
            case FFA:
                lore.add("&f● &bKills: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getKills).sum());
                lore.add("&f● &bDeaths: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getDeaths).sum());
                break;
        }
        lore.add("");

        return new ItemBuilder(kit.getIcon())
                .name("&b&l" + kit.getDisplayName())
                .durability(kit.getIconData())
                .lore(lore)
                .build();
    }
}
