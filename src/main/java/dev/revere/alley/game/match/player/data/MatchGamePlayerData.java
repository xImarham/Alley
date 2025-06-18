package dev.revere.alley.game.match.player.data;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.player.enums.EnumBaseRaiderRole;
import lombok.Getter;
import lombok.Setter;

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
    private int lives;
    private int score;
    private int kills;
    private int deaths;
    private EnumBaseRaiderRole role;

    public MatchGamePlayerData() {
        this.lives = Alley.getInstance().getConfigService().getSettingsConfig().getInt("game.lives", 3);
        this.score = 0;
    }

    /**
     * Method to handle an attack.
     */
    public void handleAttack() {
        this.hits++;
        this.combo++;

        if (this.combo > this.longestCombo) {
            this.longestCombo = this.combo;
        }
    }

    /**
     * Method to reset the combo.
     */
    public void resetCombo() {
        this.combo = 0;
    }

    public void incrementScore() {
        this.score++;
    }

    public void incrementKills() {
        this.kills++;
    }

    public void incrementDeaths() {
        this.deaths++;
    }
}