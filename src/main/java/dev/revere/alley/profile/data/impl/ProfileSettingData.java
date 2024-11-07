package dev.revere.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.profile.enums.EnumWorldTime;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @date 25/05/2024 - 15:22
 */
@Getter
@Setter
public class ProfileSettingData {
    private boolean partyMessagesEnabled;
    private boolean partyInvitesEnabled;
    private boolean scoreboardEnabled;
    private boolean tablistEnabled;
    private String chatChannel;
    private String time;

    /**
     * Constructor for the ProfileSettingData class.
     */
    public ProfileSettingData() {
        this.partyMessagesEnabled = true;
        this.partyInvitesEnabled = true;
        this.scoreboardEnabled = true;
        this.tablistEnabled = true;
        this.chatChannel = EnumChatChannel.GLOBAL.toString();
        this.time = EnumWorldTime.DEFAULT.getName();
    }

    /**
     * Set the world time for a player to the default time.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeDefault(Player player) {
        this.time = EnumWorldTime.DEFAULT.getName();
        player.resetPlayerTime();
    }

    /**
     * Set the world time for a player to day.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeDay(Player player) {
        this.time = EnumWorldTime.DAY.getName();
        player.setPlayerTime(6000L, false);
    }

    /**
     * Set the world time for a player to sunset.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeSunset(Player player) {
        this.time = EnumWorldTime.SUNSET.getName();
        player.setPlayerTime(12000, false);
    }

    /**
     * Set the world time for a player to night.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeNight(Player player) {
        this.time = EnumWorldTime.NIGHT.getName();
        player.setPlayerTime(18000L, false);
    }

    /**
     * Get the world time based on the profile setting.
     *
     * @return The world time based on the profile setting.
     */
    public EnumWorldTime getWorldTime() {
        return EnumWorldTime.getByName(this.time);
    }

    /**
     * Set the world time based on the profile setting.
     *
     * @param player The player to set the world time for.
     */
    public void setTimeBasedOnProfileSetting(Player player) {
        switch (this.getWorldTime()) {
            case DEFAULT:
                this.setTimeDefault(player);
                break;
            case DAY:
                this.setTimeDay(player);
                break;
            case SUNSET:
                this.setTimeSunset(player);
                break;
            case NIGHT:
                this.setTimeNight(player);
                break;
        }
    }

    /**
     * Check if the player is in day time.
     *
     * @return True if the player is in day time.
     */
    public boolean isDayTime() {
        return this.time.equals(EnumWorldTime.DAY.getName());
    }

    /**
     * Check if the player is in sunset time.
     *
     * @return True if the player is in sunset time.
     */
    public boolean isSunsetTime() {
        return this.time.equals(EnumWorldTime.SUNSET.getName());
    }

    /**
     * Check if the player is in night time.
     *
     * @return True if the player is in night time.
     */
    public boolean isNightTime() {
        return this.time.equals(EnumWorldTime.NIGHT.getName());
    }

    /**
     * Check if the player is in default time.
     *
     * @return True if the player is in default time.
     */
    public boolean isDefaultTime() {
        return this.time.equals(EnumWorldTime.DEFAULT.getName());
    }
}