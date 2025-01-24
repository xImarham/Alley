package dev.revere.alley.profile.stats.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.util.data.item.ItemBuilder;
import dev.revere.alley.api.menu.Button;
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
                .name("&b&lGlobal")
                .lore(
                        "",
                        "&b&lUnranked",
                        "&f● &bWins: &f" + profile.getProfileData().getUnrankedWins(),
                        "&f● &bLosses: &f" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&b&lRanked",
                        "&f● &bWins: &f" + profile.getProfileData().getRankedWins(),
                        "&f● &bLosses: &f" + profile.getProfileData().getRankedLosses(),
                        "&f● &bElo: &f" + profile.getProfileData().getElo(),
                        "",
                        "&b&lFFA",
                        "&f● &bKills: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getKills).sum(),
                        "&f● &bDeaths: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getDeaths).sum(),
                        ""

                )
                .build();
    }
}
