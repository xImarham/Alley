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
    public int getKdr() {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return this.deaths == 0 ? this.kills : Integer.parseInt(decimalFormat.format((double) this.kills / this.deaths));
    }
}