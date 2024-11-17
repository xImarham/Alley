package dev.revere.alley.profile.data.impl;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Emmy
 * @project Alley
 * @date 17/11/2024 - 12:51
 */
@Getter
@Setter
public class ProfileUnrankedKitData {
    private int wins = 0;
    private int losses = 0;

    public void incrementWins() {
        this.wins++;
    }

    public void incrementLosses() {
        this.losses++;
    }
}