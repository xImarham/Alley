package dev.revere.alley.visual.scoreboard.theme;

import lombok.Getter;
import org.bukkit.ChatColor;

/**
 * @author Emmy
 * @project Alley
 * @date 10/11/2024 - 10:27
 */
@Getter
public enum EnumScoreboardTheme {
    AQUA(ChatColor.AQUA),
    PINK(ChatColor.LIGHT_PURPLE)

    ;

    private final ChatColor color;

    /**
     * Constructor for EnumScoreboardTheme class.
     *
     * @param color The color of the theme
     */
    EnumScoreboardTheme(ChatColor color) {
        this.color = color;
    }
}