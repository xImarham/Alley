package dev.revere.alley.profile.menu.setting.enums;

import dev.revere.alley.profile.data.impl.ProfileSettingData;
import dev.revere.alley.tool.visual.LoreHelper;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.function.Function;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
public enum EnumPracticeSettingType {

    PARTY_MESSAGES(10, "&b&lToggle Party Messages", Material.FEATHER,
            settings -> Arrays.asList(
                    "&fSee party chat messages.",
                    "",
                    " " + LoreHelper.enabledOrDisabled(settings.isPartyMessagesEnabled()),
                    "",
                    "&aClick to change!"
            )
    ),

    PARTY_INVITES(11, "&b&lToggle Party Invites", Material.NAME_TAG,
            settings -> Arrays.asList(
                    "&fReceive party invites.",
                    "",
                    " " + LoreHelper.enabledOrDisabled(settings.isPartyInvitesEnabled()),
                    "",
                    "&aClick to change!"
            )
    ),

    SIDEBAR_VISIBILITY(12, "&b&lSidebar Visibility", Material.CARPET, 5,
            settings -> Arrays.asList(
                    "&fSee the scoreboard.",
                    "",
                    " " + LoreHelper.shownOrHidden(settings.isScoreboardEnabled()),
                    "",
                    "&aClick to change!"
            )
    ),

    TAB_VISIBILITY(13, "&b&lTablist Visibility", Material.ITEM_FRAME,
            settings -> Arrays.asList(
                    "&fSee the tablist.",
                    "",
                    " " + LoreHelper.shownOrHidden(settings.isTablistEnabled()),
                    "",
                    "&aClick to change!"
            )
    ),

    WORLD_TIME(14, "&b&lWorld time", Material.WATCH, settings -> Arrays.asList(
            "&fChange your world time.",
            "",
            formatTime("Default", settings.isDefaultTime(), "&a&l"),
            formatTime("Day", settings.isDayTime(), "&e&l"),
            formatTime("Sunset", settings.isSunsetTime(), "&6&l"),
            formatTime("Night", settings.isNightTime(), "&4&l"),
            "",
            "&aClick to change!"
    )),

    SCOREBOARD_LINES(19, "&b&lShow Scoreboard Lines", Material.STRING,
            settings -> Arrays.asList(
                    "&fShow scoreboard lines.",
                    "",
                    " " + LoreHelper.shownOrHidden(settings.isShowScoreboardLines()),
                    "",
                    "&aClick to change!"
            )
    ),

    PROFANITY_FILTER(20, "&b&lProfanity Filter", Material.ROTTEN_FLESH,
            settings -> Arrays.asList(
                    "&fHide rude and offensive words.",
                    "",
                    " " + LoreHelper.enabledOrDisabled(settings.isProfanityFilterEnabled()),
                    "",
                    "&aClick to change!"
            )
    ),

    MATCH_SETTINGS(16, "&b&lMatch Settings", Material.BOOK,
            settings -> Arrays.asList(
                    "&fAdjust your match settings.",
                    "",
                    "&aClick to view!"
            )
    ),

    COSMETICS(25, "&b&lCosmetics", Material.NETHER_STAR,
            settings -> Arrays.asList(
                    "&fCustomize your cosmetics.",
                    "",
                    "&aClick to view!"
            )
    );

    public final int slot;
    public final String displayName;
    public final Material material;
    public final int durability;
    public final Function<ProfileSettingData, List<String>> loreProvider;

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    EnumPracticeSettingType(int slot, String displayName, Material material, Function<ProfileSettingData, List<String>> loreProvider) {
        this(slot, displayName, material, 0, loreProvider);
    }

    /**
     * Constructor for the EnumPracticeSettingType enum.
     *
     * @param slot         The slot of the item in the menu.
     * @param displayName  The display name of the item.
     * @param material     The material of the item.
     * @param durability   The durability of the item.
     * @param loreProvider A function that provides the lore for the item based on ProfileSettingData.
     */
    EnumPracticeSettingType(int slot, String displayName, Material material, int durability, Function<ProfileSettingData, List<String>> loreProvider) {
        this.slot = slot;
        this.displayName = displayName;
        this.material = material;
        this.durability = durability;
        this.loreProvider = loreProvider;
    }

    /**
     * Formats the time string based on the active status.
     *
     * @param label       The label to display.
     * @param active      Whether the time is active or not.
     * @param activeColor The color for the active state.
     * @return The formatted time string.
     */
    private static String formatTime(String label, boolean active, String activeColor) {
        return " &f● " + (active ? activeColor : "&7") + label;
    }
}
