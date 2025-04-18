package dev.revere.alley.game.bot.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@Getter
public enum EnumBotPreset {
    EASY("Easy", 3.0, 250, 50),
    MEDIUM("Medium", 3.5, 200, 25),
    HARD("Hard", 4.0, 100, 16),
    INSANE("Insane", 4.5, 50, 8),
    LEGENDARY("Legendary", 5.0, 25, 4),
    GODLIKE("Godlike", 5.5, 10, 2),
    UNSTOPPABLE("Unstoppable", 6.0, 5, 1);

    private final String name;
    private final double reach;
    private final int reactionTime;
    private final int aimRandomness;

    /**
     * Constructor for the EnumBotPreset enum.
     *
     * @param name          The name of the preset.
     * @param reach         The reach of the bot.
     * @param reactionTime  The reaction time of the bot.
     * @param aimRandomness The aim randomness of the bot.
     */
    EnumBotPreset(String name, double reach, int reactionTime, int aimRandomness) {
        this.name = name;
        this.reach = reach;
        this.reactionTime = reactionTime;
        this.aimRandomness = aimRandomness;
    }
}