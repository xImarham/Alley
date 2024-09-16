package me.emmy.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;

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
}
