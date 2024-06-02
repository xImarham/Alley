package me.emmy.alley.profile.data;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.impl.*;

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
    private ProfileCosmeticData profileCosmeticData;
    private ProfileDivisionData profileDivisionData;

    private int coins = Profile.DEFAULT_COINS;
    private int unrankedWins = 0;
    private int unrankedLosses = 0;
    private int rankedWins = 0;
    private int rankedLosses = 0;

    /**
     * Constructor for the ProfileData class
     */
    public ProfileData() {
        initializeMaps();
        feedDataClasses();
        initializeDataClasses();
    }

    /**
     * Initializes the data classes
     */
    private void initializeDataClasses() {
        this.profileSettingData = new ProfileSettingData();
        this.profileCosmeticData = new ProfileCosmeticData();
        this.profileDivisionData = new ProfileDivisionData();
    }

    /**
     * Feeds the data classes with the kits and ffa matches
     */
    private void feedDataClasses() {
        Alley.getInstance().getKitRepository().getKits().forEach(kit -> this.kitData.put(kit.getName(), new ProfileKitData()));
        Alley.getInstance().getFfaRepository().getMatches().forEach(kit -> this.ffaData.put(kit.getName(), new ProfileFFAData()));
    }

    /**
     * Initializes the maps
     */
    private void initializeMaps() {
        this.kitData = Maps.newHashMap();
        this.ffaData = Maps.newHashMap();
    }
}
