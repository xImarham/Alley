package dev.revere.alley.util.visual;

import dev.revere.alley.util.chat.CC;
import lombok.experimental.UtilityClass;

/**
 * @author Emmy
 * @project Alley
 * @date 05/10/2024 - 11:05
 */
@UtilityClass
public class ScoreboardUtil {
    /**
     * Visualizes the goals in a scoreboard format.
     *
     * @param currentGoals The current number of goals achieved.
     * @param maxGoals     The maximum number of goals to visualize.
     * @return A string representation of the goals, with filled and empty indicators.
     */
    public String visualizeGoals(int currentGoals, int maxGoals) {
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