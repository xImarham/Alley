package me.emmy.alley.match.player.data;

import jdk.nashorn.internal.runtime.regexp.joni.Config;
import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.config.ConfigHandler;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public class MatchGamePlayerData {

    private int longestCombo;
    private int combo;
    private int hits;
    private int lives = ConfigHandler.getInstance().getSettingsConfig().getInt("game.lives");

    /**
     * Method to handle an attack.
     */
    public void handleAttack() {
        hits++;
        combo++;

        if (combo > longestCombo) {
            longestCombo = combo;
        }
    }

    /**
     * Method to reset the combo.
     */
    public void resetCombo() {
        combo = 0;
    }
}
