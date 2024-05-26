package me.emmy.alley.leaderboard.menu.leaderboard.button;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
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
        if (leaderboardType == EnumLeaderboardType.RANKED) {
            lore.add("&f● &dWins: &f" + profile.getProfileData().getRankedWins());
            lore.add("&f● &dLosses: &f" + profile.getProfileData().getRankedLosses());
            lore.add("&f● &dElo: &f" + profile.getProfileData().getElo());
        } else if (leaderboardType == EnumLeaderboardType.UNRANKED) {
            lore.add("&f● &dWins: &f" + profile.getProfileData().getUnrankedWins());
            lore.add("&f● &dLosses: &f" + profile.getProfileData().getUnrankedLosses());
        } else if (leaderboardType == EnumLeaderboardType.FFA) {
            lore.add("&f● &dKills: &f" + profile.getProfileData().getFfaKills());
            lore.add("&f● &dDeaths: &f" + profile.getProfileData().getFfaDeaths());
        }
        lore.add("");

        return new ItemBuilder(kit.getIcon())
                .name("&d&l" + kit.getDisplayName())
                .lore(lore)
                .build();
    }
}
