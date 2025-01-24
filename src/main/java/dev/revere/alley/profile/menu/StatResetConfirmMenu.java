package dev.revere.alley.profile.menu;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.util.data.item.ItemBuilder;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.util.chat.CC;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 02/01/2025 - 21:08
 */
@AllArgsConstructor
public class StatResetConfirmMenu extends Menu {
    private final UUID uuid;

    @Override
    public String getTitle(Player player) {
        return "&cReset stats?";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new ConfirmButton(this.uuid));
        buttons.put(15, new CancelButton());

        return buttons;
    }

    @Override
    public int getSize() {
        return 3 * 9;
    }

    @AllArgsConstructor
    private static class ConfirmButton extends Button {
        private final UUID uuid;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.EMERALD_BLOCK)
                    .name("&aConfirm")
                    .lore(
                            "",
                            "&7Click here to confirm the reset of",
                            "&7the stats of " + Bukkit.getPlayer(this.uuid).getName() + "."
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(this.uuid);
            profile.setProfileData(new ProfileData());
            profile.save();

            player.sendMessage(CC.translate("&aSuccessfully reset stats for &e" + Bukkit.getPlayer(this.uuid).getName() + "&a."));
        }
    }

    @AllArgsConstructor
    private static class CancelButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.REDSTONE_BLOCK)
                    .name("&cCancel")
                    .lore(
                            "",
                            "&7Click here to cancel the reset."
                    )
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
        }
    }
}