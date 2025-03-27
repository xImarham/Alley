package dev.revere.alley.feature.scoreboard;

import dev.revere.alley.util.animator.TextAnimator;
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