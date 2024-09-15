package me.emmy.alley.profile.shop.menu.impl;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.cosmetic.interfaces.ICosmetic;
import me.emmy.alley.util.chat.CC;
import me.emmy.alley.util.item.ItemBuilder;
import me.emmy.alley.api.menu.Button;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class ShopEffectButton extends Button {

    private final ICosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        String lore;
        if (hasPermission) {
            lore = "&fYou already own this cosmetic.";
        } else {
            lore = "&cClick to purchase this cosmetic for &b" + cosmetic.getPrice() + " coins.";
        }

        return new ItemBuilder(cosmetic.getIcon())
                .name("&b&l" + cosmetic.getName())
                .lore(
                        "",
                        "&f● &bDescription: &f" + cosmetic.getDescription(),
                        "&f● &bPrice: &f" + cosmetic.getPrice() + " coins",
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
        if (player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou already own this cosmetic."));
            return;
        }

        if (profile.getProfileData().getCoins() < cosmetic.getPrice()) {
            player.sendMessage(CC.translate("&cYou do not have enough coins to purchase this cosmetic."));
            return;
        }

        profile.getProfileData().setCoins(profile.getProfileData().getCoins() - cosmetic.getPrice());


        FileConfiguration config = ConfigHandler.getInstance().getSettingsConfig();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.get("command.grant-cosmetic-permission-command").toString().replace("{player}", player.getName()).replace("%permission%", cosmetic.getPermission()));

        player.sendMessage(CC.translate("&aYou have successfully purchased the " + cosmetic.getName() + " cosmetic for &b" + cosmetic.getPrice() + " coins."));
    }
}
