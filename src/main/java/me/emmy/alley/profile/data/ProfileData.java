package me.emmy.alley.profile.data;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.killeffects.AbstractKillEffect;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.ProfileFFAData;
import me.emmy.alley.profile.data.impl.ProfileKitData;
import me.emmy.alley.profile.data.impl.ProfileSettingData;

import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 21/05/2024 - 22:03
 */

@Getter
@Setter
public class ProfileData {

    private Map<String, ProfileKitData> kitData;
    private Map<String, ProfileFFAData> ffaData;
    private ProfileSettingData profileSettingData;

    private AbstractKillEffect activeKillEffect;

    private int coins = Profile.DEFAULT_COINS;
    private int elo = Profile.DEFAULT_ELO;
    private int unrankedWins = 0;
    private int unrankedLosses = 0;
    private int rankedWins = 0;
    private int rankedLosses = 0;
    private int ffaKills = 0;
    private int ffaDeaths = 0;

    public ProfileData() {
        this.profileSettingData = new ProfileSettingData();
        this.kitData = Maps.newHashMap();
        this.ffaData = Maps.newHashMap();
        Alley.getInstance().getKitRepository().getKits().forEach(kit -> this.kitData.put(kit.getName(), new ProfileKitData()));
        Alley.getInstance().getFfaRepository().getMatches().forEach(kit -> this.ffaData.put(kit.getName(), new ProfileFFAData()));
    }

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

    public void addFFAKill() {
        this.ffaKills++;
    }

    public void addFFALosses() {
        this.ffaDeaths++;
    }
}
