package me.emmy.alley.profile.settings.menu;

import lombok.AllArgsConstructor;
import me.emmy.alley.Alley;
import me.emmy.alley.api.menu.Button;
import me.emmy.alley.api.menu.Menu;
import me.emmy.alley.api.menu.pagination.ItemBuilder;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @date 23/05/2024 - 01:27
 */
@AllArgsConstructor
public class SettingsMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return "Settings";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

        //buttons.put(0, new BackButton(new ProfileMenu()));

        buttons.put(10, new SettingsButton("&b&lToggle Party Messages", Material.FEATHER, 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &bparty chat",
                "&fmessages or not.",
                "",
                " &b● &fStatus: &r" + (profile.getProfileData().getProfileSettingData().isPartyMessagesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(11, new SettingsButton("&b&lToggle Party Invites", Material.NAME_TAG, 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&freceive &bparty invites",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (profile.getProfileData().getProfileSettingData().isPartyInvitesEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(12, new SettingsButton("&b&lToggle Scoreboard", Material.CARPET, 5, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &bscoreboard",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (profile.getProfileData().getProfileSettingData().isScoreboardEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(13, new SettingsButton("&b&lToggle Tablist", Material.ITEM_FRAME, 0, Arrays.asList(
                "",
                "&fDecide whether you should",
                "&fsee the &btablist",
                "&for not.",
                "",
                " &b● &fStatus: &r" + (profile.getProfileData().getProfileSettingData().isTablistEnabled() ? "&aEnabled" : "&cDisabled"),
                "",
                "&aClick to toggle!"
        )));

        buttons.put(14, new SettingsButton("&b&lWorld time", Material.WATCH, 0, Arrays.asList(
                "",
                "&fChange your world time",
                "&fto &bday&f, &bnight&f, or &bsunset&f.",
                "",
                profile.getProfileData().getProfileSettingData().isDefaultTime() ? " &b● &a&lDefault" : " &b● &7Default",
                profile.getProfileData().getProfileSettingData().isDayTime() ? " &b● &e&lDay" : " &b● &7Day",
                profile.getProfileData().getProfileSettingData().isSunsetTime() ? " &b● &6&lSunset" : " &b● &7Sunset",
                profile.getProfileData().getProfileSettingData().isNightTime() ? " &b● &4&lNight" : " &b● &7Night",
                "",
                "&aClick to change!"
        )));

        addBorder(buttons, (byte) 15, 3);

        return buttons;
    }

    @Override
    public int getSize() {
        return 9 * 3;
    }

    @AllArgsConstructor
    private static class SettingsButton extends Button {
        private String displayName;
        private Material material;
        private int durability;
        private List<String> lore;

        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(material)
                    .name(displayName)
                    .durability(durability)
                    .lore(lore)
                    .hideMeta()
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            Profile profile = Alley.getInstance().getProfileRepository().getProfile(player.getUniqueId());

            if (clickType == ClickType.LEFT) {
                switch (material) {
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
                    case ITEM_FRAME:
                        player.performCommand("toggletablist");
                        break;
                    case WATCH:
                        switch (profile.getProfileData().getProfileSettingData().getWorldTime()) {
                            case DEFAULT:
                                profile.getProfileData().getProfileSettingData().setTimeDay(player);
                                player.sendMessage(CC.translate("&aYou have set the time to day."));
                                break;
                            case DAY:
                                profile.getProfileData().getProfileSettingData().setTimeSunset(player);
                                player.sendMessage(CC.translate("&aYou have set the time to sunset."));
                                break;
                            case SUNSET:
                                profile.getProfileData().getProfileSettingData().setTimeNight(player);
                                player.sendMessage(CC.translate("&aYou have set the time to night."));
                                break;
                            case NIGHT:
                                profile.getProfileData().getProfileSettingData().setTimeDefault(player);
                                player.sendMessage(CC.translate("&aYou have reset your world time."));
                                break;
                        }
                        break;
                }

                playerClickSound(player);
            } else if (clickType == ClickType.RIGHT) {
                if (Objects.requireNonNull(material) == Material.WATCH) {
                    switch (profile.getProfileData().getProfileSettingData().getWorldTime()) {
                        case DEFAULT:
                            profile.getProfileData().getProfileSettingData().setTimeNight(player);
                            player.sendMessage(CC.translate("&aYou have set the time to night."));
                            break;
                        case DAY:
                            profile.getProfileData().getProfileSettingData().setTimeDefault(player);
                            player.sendMessage(CC.translate("&aYou have reset your world time."));
                            break;
                        case SUNSET:
                            profile.getProfileData().getProfileSettingData().setTimeDay(player);
                            player.sendMessage(CC.translate("&aYou have set the time to day."));
                            break;
                        case NIGHT:
                            profile.getProfileData().getProfileSettingData().setTimeSunset(player);
                            player.sendMessage(CC.translate("&aYou have set the time to sunset."));
                            break;
                    }

                    playerClickSound(player);
                }
            }
        }
    }
}