package me.emmy.alley.visual.leaderboard.menu.leaderboard.button;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.LeaderboardMenu;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.item.ItemBuilder;
import me.emmy.alley.api.menu.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@AllArgsConstructor
public class DisplayTypeButton extends Button {

    /**
     * Gets the item to display in the menu.
     *
     * @param player the player viewing the menu
     * @return the item to display
     */
    @Override
    public ItemStack getButtonItem(Player player) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());
        EnumLeaderboardType currentType = profile.getLeaderboardType();

        List<String> lore = new ArrayList<>();
        lore.add("");
        for (EnumLeaderboardType type : EnumLeaderboardType.values()) {
            lore.add((currentType == type ? "&f● &b" : "&f● &7") + type.getName());
        }
        lore.add("");
        lore.add("&fClick to change the display type.");

        return new ItemBuilder(Material.EYE_OF_ENDER)
                .name("&b&lDisplay Type")
                .lore(lore)
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
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        EnumLeaderboardType currentType = profile.getLeaderboardType();
        EnumLeaderboardType[] types = EnumLeaderboardType.values();
        int currentIndex = currentType.ordinal();

        switch (clickType) {
            case LEFT:
                currentIndex = (currentIndex + 1) % types.length;
                break;
            case RIGHT:
                currentIndex = (currentIndex - 1 + types.length) % types.length;
                break;
        }

        EnumLeaderboardType newType = types[currentIndex];
        profile.setLeaderboardType(newType);
        new LeaderboardMenu().openMenu(player);
        playNeutral(player);
    }
}
