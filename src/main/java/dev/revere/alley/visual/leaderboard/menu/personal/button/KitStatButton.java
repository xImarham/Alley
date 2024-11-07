package dev.revere.alley.visual.leaderboard.menu.personal.button;

import lombok.AllArgsConstructor;
import dev.revere.alley.Alley;
import dev.revere.alley.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.ProfileKitData;
import dev.revere.alley.util.item.ItemBuilder;
import dev.revere.alley.api.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class KitStatButton extends Button {

    private final Kit kit;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        ProfileKitData profileKitData = profile.getProfileData().getKitData().get(kit.getName());

        return new ItemBuilder(kit.getIcon())
                .name("&b&l" + kit.getDisplayName())
                .durability(kit.getIconData())
                .lore(
                        "",
                        "&f● &bWins: &f" + profileKitData.getWins(),
                        "&f● &bLosses: &f" + profileKitData.getLosses(),
                        "&f● &bElo: &f" + profileKitData.getElo(),
                        ""
                )
                .build();
    }
}
