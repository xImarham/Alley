package me.emmy.alley.scoreboard.handler;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.task.TaskUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 27/03/2024 - 14:55
 */

@Getter @Setter
public class ScoreboardHandler {

    FileConfiguration scoreboardConfig = Alley.getInstance().getConfigHandler().getConfigByName("providers/scoreboard.yml");

    private String text;
    private boolean animated;
    private long ticks;
    private List<String> animation;

    public ScoreboardHandler(){
        text = scoreboardConfig.getString("scoreboard.title.text", "null");
        animated = scoreboardConfig.getBoolean("scoreboard.title.animated");
        ticks = scoreboardConfig.getLong("scoreboard.title.ticks");
        if (animated){
            animation = new ArrayList<>();
            animation = scoreboardConfig.getStringList("scoreboard.title.animation");
            runTitleAnimation();
        }
    }

    int i = 0;

    private void runTitleAnimation(){
        TaskUtil.runTaskTimer(() -> {
            text = animation.get(i);
            i++;
            if (i == animation.size()) i = 0;
        }, ticks, ticks);
    }

}