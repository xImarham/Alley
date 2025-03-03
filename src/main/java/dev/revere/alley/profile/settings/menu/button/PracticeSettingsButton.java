package dev.revere.alley.profile.settings.menu.button;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Button;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumWorldTime;
import dev.revere.alley.profile.settings.menu.MatchSettingsMenu;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.data.item.ItemBuilder;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 02/03/2025
 */
@AllArgsConstructor
public class PracticeSettingsButton extends Button {
    private String displayName;
    private Material material;
    private int durability;
    private List<String> lore;

    @Override
    public ItemStack getButtonItem(Player player) {
        return new ItemBuilder(this.material)
                .name(this.displayName)
                .durability(this.durability)
                .lore(this.lore)
                .hideMeta()
                .build();
    }

    @Override
    public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        if (this.material == Material.WATCH) {
            EnumWorldTime newTime = this.getNextWorldTime(clickType, profile);
            profile.getProfileData().getProfileSettingData().setTime(newTime.getName());

            switch (newTime) {
                case DEFAULT:
                    profile.getProfileData().getProfileSettingData().setTimeDefault(player);
                    player.sendMessage(CC.translate("&aYou have reset your world time."));
                    break;
                case DAY:
                    profile.getProfileData().getProfileSettingData().setTimeDay(player);
                    player.sendMessage(CC.translate("&aYou have set the time to day."));
                    break;
                case SUNSET:
                    profile.getProfileData().getProfileSettingData().setTimeSunset(player);
                    player.sendMessage(CC.translate("&aYou have set the time to sunset."));
                    break;
                case NIGHT:
                    profile.getProfileData().getProfileSettingData().setTimeNight(player);
                    player.sendMessage(CC.translate("&aYou have set the time to night."));
                    break;
            }

            playNeutral(player);
            return;
        }

        if (clickType != ClickType.LEFT) {
            return;
        }

        switch (this.material) {
            case FEATHER:
                player.performCommand("togglepartymessages");
                break;
            case NAME_TAG:
                player.performCommand("togglepartyinvites");
                break;
            case CARPET:
                if (durability == (short) 5) {
                    player.performCommand("togglescoreboard");
                }
                break;
            case BOOK:
                new MatchSettingsMenu().openMenu(player);
                break;
            case STRING:
                player.performCommand("togglescoreboardlines");
                break;
            case ITEM_FRAME:
                player.performCommand("toggletablist");
                break;
        }

        playNeutral(player);
    }

    /**
     * Gets the next world time based on the click type.
     *
     * @param clickType the type of click
     * @param profile   the player's profile
     * @return the next world time
     */
    private EnumWorldTime getNextWorldTime(ClickType clickType, Profile profile) {
        EnumWorldTime[] timeStates = EnumWorldTime.values();
        int currentIndex = profile.getProfileData().getProfileSettingData().getWorldTime().ordinal();

        if (clickType == ClickType.LEFT) {
            currentIndex = (currentIndex + 1) % timeStates.length;
        } else if (clickType == ClickType.RIGHT) {
            currentIndex = (currentIndex - 1 + timeStates.length) % timeStates.length;
        }

        return timeStates[currentIndex];
    }
}