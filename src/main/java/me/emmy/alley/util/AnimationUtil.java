package me.emmy.alley.util;

import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 15/09/2024 - 00:09
 */
@UtilityClass
public class AnimationUtil {
    private final List<String> DOTS = Arrays.asList(".", "..", "...");
    private int dotIndex = 0;
    private long lastUpdateTime = System.currentTimeMillis();
    private final long UPDATE_INTERVAL = 500;

    /**
     * Get the current dot animation string based on the current time.
     * Updates the dot animation every UPDATE_INTERVAL milliseconds.
     *
     * @return the current dot animation string.
     */
    public String getDots() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime >= UPDATE_INTERVAL) {
            dotIndex = (dotIndex + 1) % DOTS.size();
            lastUpdateTime = currentTime;
        }
        return DOTS.get(dotIndex);
    }
}