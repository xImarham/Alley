package dev.revere.alley.tool.animation.config.impl;

import dev.revere.alley.tool.animation.config.TextAnimator;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @since 27/03/2025
 */
public class ScoreboardTitleAnimator extends TextAnimator {
    /**
     * Constructor for the TextAnimator class.
     *
     * @param config The configuration file.
     */
    public ScoreboardTitleAnimator(FileConfiguration config) {
        super(config, "scoreboard.title");
    }
}