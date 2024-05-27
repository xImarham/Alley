package me.emmy.alley.match.player.data;

import lombok.Getter;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
public class MatchGamePlayerData {

    private int longestCombo;
    private int combo;
    private int hits;

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
