package dev.revere.alley.profile.menu.shop.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.item.ItemBuilder;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/23/2025
 */
@AllArgsConstructor
public class ShopItemButton extends Button {
    private final AbstractCosmetic cosmetic;

    @Override
    public ItemStack getButtonItem(Player player) {
        boolean hasPermission = player.hasPermission(cosmetic.getPermission());

        List<String> lore = new ArrayList<>();
        lore.add("&7" + cosmetic.getDescription());
        lore.add("");

        if (hasPermission) {
            lore.add("&aYou already own this item.");
        } else {
            lore.add("&fPrice: &b" + cosmetic.getPrice() + " Coins");
            lore.add("");
            lore.add("&aClick to purchase!");
        }

        return new ItemBuilder(cosmetic.getIcon())
                .name("&b&l" + cosmetic.getName())
                .lore(lore)
                .build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Alley.getInstance().getProfileService().getProfile(player.getUniqueId());

        if (player.hasPermission(cosmetic.getPermission())) {
            player.sendMessage(CC.translate("&cYou already own this cosmetic."));
            this.playFail(player);
            return;
        }

        if (profile.getProfileData().getCoins() < cosmetic.getPrice()) {
            player.sendMessage(CC.translate("&cYou don't have enough coins to purchase this."));
            this.playFail(player);
            return;
        }

        profile.getProfileData().setCoins(profile.getProfileData().getCoins() - cosmetic.getPrice());

        FileConfiguration config = Alley.getInstance().getConfigService().getSettingsConfig();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), config.get("command.grant-cosmetic-permission-command").toString().replace("{player}", player.getName()).replace("%permission%", cosmetic.getPermission()));

        player.sendMessage(CC.translate("&aSuccessfully purchased the &b" + cosmetic.getName() + " &acosmetic!"));
        this.playSuccess(player);
    }
}