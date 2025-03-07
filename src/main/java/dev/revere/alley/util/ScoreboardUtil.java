package dev.revere.alley.util;

import lombok.experimental.UtilityClass;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 05/10/2024 - 11:05
 */
@UtilityClass
public class ScoreboardUtil {
    /**
     * Visualizes the goals as circles.
     *
     * @param currentGoals The current goals.
     * @param maxGoals     The maximum goals.
     * @return The goals visualized as circles.
     */
    public String visualizeGoalsAsCircles(int currentGoals, int maxGoals) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < maxGoals; i++) {
            if (i < currentGoals) {
                stringBuilder.append(CC.translate("&a■"));
            } else {
                stringBuilder.append(CC.translate("&7■"));
            }
        }

        return stringBuilder.toString();
    }
}