package dev.revere.alley.feature.leaderboard.menu.button;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.util.data.item.ItemBuilder;
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
        switch (this.leaderboardType) {
            case RANKED:
                lore.add("&f● &bWins: &f" + profile.getProfileData().getRankedKitData().get(this.kit.getName()).getWins());
                lore.add("&f● &bLosses: &f" + profile.getProfileData().getRankedKitData().get(this.kit.getName()).getLosses());
                lore.add("&f● &bElo: &f" + profile.getProfileData().getRankedKitData().get(this.kit.getName()).getElo());
                break;
            case UNRANKED:
                lore.add("&f● &bWins: &f" + profile.getProfileData().getUnrankedKitData().get(this.kit.getName()).getWins());
                lore.add("&f● &bLosses: &f" + profile.getProfileData().getUnrankedKitData().get(this.kit.getName()).getLosses());
                break;
            case FFA:
                lore.add("&f● &bKills: &f" + profile.getProfileData().getFfaData().get(this.kit.getName()).getKills());
                lore.add("&f● &bDeaths: &f" + profile.getProfileData().getFfaData().get(this.kit.getName()).getDeaths());
                break;
        }
        lore.add("");

        return new ItemBuilder(this.kit.getIcon())
                .name("&b&l" + this.kit.getDisplayName())
                .durability(this.kit.getIconData())
                .lore(lore)
                .build();
    }
}