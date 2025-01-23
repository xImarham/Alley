package dev.revere.alley.visual.scoreboard.animation;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;
import dev.revere.alley.util.TaskUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 27/03/2024 - 14:55
 */
@Getter
@Setter
public class ScoreboardTitleHandler {
    private final FileConfiguration config;
    private List<String> animation;
    private boolean animated;
    private String text;
    private long ticks;
    private int index;

    /**
     * Constructor for the ScoreboardTitleHandler class.
     *
     * @param config The configuration file
     */
    public ScoreboardTitleHandler(FileConfiguration config) {
        this.config = config;
        this.text = this.config.getString("scoreboard.title.text", "null");
        this.animated = this.config.getBoolean("scoreboard.title.animated");
        this.ticks = this.config.getLong("scoreboard.title.ticks");
        this.index = 0;

        if (this.animated) {
            this.animation = new ArrayList<>();
            this.animation = this.config.getStringList("scoreboard.title.animation");
            this.runTitleAnimation();
        }
    }

    private void runTitleAnimation() {
        TaskUtil.runTaskTimer(() -> {
            this.text = this.animation.get(this.index);
            this.index++;
            if (this.index == this.animation.size()) this.index = 0;
        }, this.ticks, this.ticks);
    }
}