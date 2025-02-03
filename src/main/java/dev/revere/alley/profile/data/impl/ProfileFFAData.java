package dev.revere.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;

import java.text.DecimalFormat;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public class ProfileFFAData {
    private int kills = 0;
    private int deaths = 0;

    public void incrementKills() {
        this.kills++;
    }

    public void incrementDeaths() {
        this.deaths++;
    }

    /**
     * Get the kill/death ratio.
     *
     * @return the kill/death ratio
     */
    public String getKillDeathRatio() {
        if (this.deaths == 0) {
            return this.kills == 0 ? "0.0x" : String.format("%.1f", (double) this.kills) + "x";
        }

        double ratio = (double) this.kills / this.deaths;

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(ratio) + "x";
    }
}