package me.emmy.alley.profile;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 22:03
 */

@Getter
@Setter
public class ProfileData {
    private int coins = Profile.DEFAULT_COINS;
    private int elo = Profile.DEFAULT_ELO;
    private int unrankedWins = 0;
    private int unrankedLosses = 0;
    private int rankedWins = 0;
    private int rankedLosses = 0;

    public void addUnrankedWins() {
        this.unrankedWins++;
    }

    public void addUnrankedLosses() {
        this.unrankedLosses++;
    }

    public void addRankedWins() {
        this.rankedWins++;
    }

    public void addRankedLosses() {
        this.rankedLosses++;
    }

    public void addElo() {
        this.elo++;
    }

    public void addCoins() {
        this.coins++;
    }

}
