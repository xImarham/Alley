package me.emmy.alley.profile.division.menu;

import com.sk89q.worldedit.EditSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.ProfileFFAData;
import me.emmy.alley.profile.division.AbstractDivision;
import me.emmy.alley.utils.item.ItemBuilder;
import me.emmy.alley.utils.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 6/2/2024
 */
@Getter
@AllArgsConstructor
public class DivisionButton extends Button {

    private final AbstractDivision division;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        AbstractDivision abstractDivision = Alley.getInstance().getDivisionRepository().getDivision(profile.getProfileData().getProfileDivisionData().getDivision());
        return new ItemBuilder(division.getIcon())
                .name("&d&l" + division.getName())
                .lore(
                        "",
                        "&d&lYour Progress",
                        "&f● &dGlobal Elo: &f" + profile.getProfileData().getProfileDivisionData().getGlobalElo(),
                        "&f● &dTier: &f" + abstractDivision.getTier().getName(),
                        "&f● &dLevel: &f" + abstractDivision.getLevel().getName(),
                        "",
                        "&d&lDivision Information",
                        "&f● &dTier: &f" + division.getTier().getName(),
                        "&f● &dLevel: &f" + division.getLevel().getName(),
                        "&f● &dElo Range: &f" + division.getEloMin() + " - " + division.getEloMax(),
                        "&f● &dDescription: &f" + division.getDescription(),
                        ""

                )
                .build();
    }
}
