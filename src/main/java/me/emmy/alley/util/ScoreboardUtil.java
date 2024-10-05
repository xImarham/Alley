package me.emmy.alley.util;

import lombok.experimental.UtilityClass;
import me.emmy.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 05/10/2024 - 11:05
 */
@UtilityClass
public class ScoreboardUtil {
    /**
     * Visualizes the lives of a player in a circle format.
     *
     * @param lives The amount of lives to visualize.
     * @return The visualized lives.
     */
    public String visualizeLivesAsCircles(int lives) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            if (i < lives) {
                stringBuilder.append(CC.translate("&a&l●"));
            } else {
                stringBuilder.append(CC.translate("&7&l●"));
            }
        }

        return stringBuilder.toString();
    }
}