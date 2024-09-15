package me.emmy.alley.profile.cosmetic.menu.button;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.cosmetic.impl.soundeffect.AbstractSoundEffect;
import me.emmy.alley.profile.cosmetic.impl.killeffects.AbstractKillEffect;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmetic;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.item.ItemBuilder;
import me.emmy.alley.api.menu.Button;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class CosmeticButton extends Button {

    private final ICosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());
        boolean isSelected = profile.getProfileData().getProfileCosmeticData().isSelectedCosmetic(cosmetic);

        String lore;
        if (hasPermission) {
            lore = isSelected ? "&cYou already have this cosmetic selected." : "&fClick to select this cosmetic.";
        } else {
            lore = "&cYou do not have permission to select this cosmetic.";
        }

        return new ItemBuilder(cosmetic.getIcon())
                .name("&b&l" + cosmetic.getName())
                .lore(
                        "",
                        "&f● &bDescription: &f" + cosmetic.getDescription(),
                        "",
                        lore

                )
                .build();
    }

    /**
     * Handles the click event for the button.
     *
     * @param player the player who clicked the button
     * @param clickType the type of click
     */
    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType == ClickType.MIDDLE || clickType == ClickType.RIGHT || clickType == ClickType.NUMBER_KEY || clickType == ClickType.DROP || clickType == ClickType.SHIFT_LEFT || clickType == ClickType.SHIFT_RIGHT) {
            return;
        }
        playNeutral(player);

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        if (profile.getProfileData().getProfileCosmeticData().isSelectedCosmetic(cosmetic)) {
            player.sendMessage(CC.translate("&cYou already have this cosmetic selected."));
            return;
        }

        if (!player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou do not have permission to select this cosmetic."));
            return;
        }

        if (cosmetic instanceof AbstractKillEffect) {
            profile.getProfileData().getProfileCosmeticData().setSelectedKillEffect(cosmetic.getName());
        } else if (cosmetic instanceof AbstractSoundEffect) {
            profile.getProfileData().getProfileCosmeticData().setSelectedSoundEffect(cosmetic.getName());
        }

        player.sendMessage(CC.translate("&aYou have successfully selected the &b" + cosmetic.getName() + " &acosmetic."));
    }
}
