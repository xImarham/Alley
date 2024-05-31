package me.emmy.alley.leaderboard.menu.personal.button;

import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.ProfileFFAData;
import me.emmy.alley.profile.data.impl.ProfileKitData;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class GlobalStatButton extends Button {

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        return new ItemBuilder(Material.NETHER_STAR)
                .name("&d&lGlobal")
                .lore(
                        "",
                        "&d&lRanked Kit Statistics",
                        "&f● &dWins: &f" + profile.getProfileData().getRankedWins(),
                        "&f● &dLosses: &f" + profile.getProfileData().getRankedLosses(),
                        "&f● &dElo: &f" + profile.getProfileData().getElo(),
                        "",
                        "&d&lUnranked Kit Statistics",
                        "&f● &dWins: &f" + profile.getProfileData().getUnrankedWins(),
                        "&f● &dLosses: &f" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&d&lFFA Statistics",
                        "&f● &dKills: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getKills).sum(),
                        "&f● &dDeaths: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getDeaths).sum(),
                        ""

                )
                .build();
    }
}
