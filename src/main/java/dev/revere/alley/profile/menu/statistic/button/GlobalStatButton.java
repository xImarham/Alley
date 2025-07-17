package dev.revere.alley.profile.menu.statistic.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.tool.item.ItemBuilder;
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
        Profile profile = Alley.getInstance().getService(IProfileService.class).getProfile(player.getUniqueId());
        return new ItemBuilder(Material.NETHER_STAR)
                .name("&6&lGlobal")
                .lore(
                        "",
                        "&6&lUnranked",
                        "&f● &6Wins: &f" + profile.getProfileData().getUnrankedWins(),
                        "&f● &6Losses: &f" + profile.getProfileData().getUnrankedLosses(),
                        "",
                        "&6&lRanked",
                        "&f● &6Wins: &f" + profile.getProfileData().getRankedWins(),
                        "&f● &6Losses: &f" + profile.getProfileData().getRankedLosses(),
                        "&f● &6Elo: &f" + profile.getProfileData().getElo(),
                        "",
                        "&6&lFFA",
                        "&f● &6Kills: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getKills).sum(),
                        "&f● &6Deaths: &f" + profile.getProfileData().getFfaData().values().stream().mapToInt(ProfileFFAData::getDeaths).sum(),
                        ""

                )
                .build();
    }
}
