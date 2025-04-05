package dev.revere.alley.profile.data;

import com.google.common.collect.Maps;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.impl.*;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 21/05/2024 - 22:03
 */
@Getter
@Setter
public class ProfileData {
    private Map<String, ProfileUnrankedKitData> unrankedKitData;
    private Map<String, ProfileRankedKitData> rankedKitData;
    private Map<String, ProfileFFAData> ffaData;
    private ProfileSettingData profileSettingData;
    private ProfileCosmeticData profileCosmeticData;
    private ProfilePlayTimeData profilePlayTimeData;

    private int elo = 1000;
    private int coins = 100;
    private int unrankedWins = 0;
    private int unrankedLosses = 0;
    private int rankedWins = 0;
    private int rankedLosses = 0;

    private boolean rankedBanned;

    public ProfileData() {
        this.initializeMaps();
        this.feedDataClasses();
        this.initializeDataClasses();
        this.rankedBanned = false;
    }

    private void initializeDataClasses() {
        this.profileSettingData = new ProfileSettingData();
        this.profileCosmeticData = new ProfileCosmeticData();
        this.profilePlayTimeData = new ProfilePlayTimeData();
    }

    private void feedDataClasses() {
        //Alley.getInstance().getKitRepository().getKits().stream().filter(Kit -> Kit.isSettingEnabled(KitSettingRankedImpl.class)).forEach(kit -> this.rankedKitData.put(kit.getName(), new ProfileRankedKitData()));
        Alley.getInstance().getKitService().getKits().forEach(kit -> this.rankedKitData.put(kit.getName(), new ProfileRankedKitData()));
        Alley.getInstance().getKitService().getKits().forEach(kit -> this.unrankedKitData.put(kit.getName(), new ProfileUnrankedKitData()));
        Alley.getInstance().getFfaService().getMatches().forEach(kit -> this.ffaData.put(kit.getName(), new ProfileFFAData()));
    }

    private void initializeMaps() {
        this.unrankedKitData = Maps.newHashMap();
        this.rankedKitData = Maps.newHashMap();
        this.ffaData = Maps.newHashMap();
    }

    /**
     * Calculates the global elo of the player
     *
     * @param profile the profile of the player
     * @return the global elo of the player
     */
    private int calculateGlobalElo(Profile profile) {
        var rankedKits = Alley.getInstance().getKitService().getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRankedImpl.class))
                .collect(Collectors.toList());

        if (rankedKits.isEmpty()) {
            return 0;
        }

        int totalElo = rankedKits.stream()
                .mapToInt(kit -> {
                    ProfileRankedKitData kitData = profile.getProfileData().getRankedKitData().get(kit.getName());
                    return kitData != null ? kitData.getElo() : 0;
                })
                .sum();

        return totalElo / rankedKits.size();
    }

    /**
     * Updates the elo and division of the player
     *
     * @param profile the profile of the player
     */
    public void updateElo(Profile profile) {
        this.elo = this.calculateGlobalElo(profile);
    }

    /**
     * Get the total amount of wins
     *
     * @return The total amount of wins
     */
    public int getTotalWins() {
        return this.rankedWins + this.unrankedWins;
    }

    /**
     * Get the total amount of losses
     *
     * @return The total amount of losses
     */
    public int getTotalLosses() {
        return this.rankedLosses + this.unrankedLosses;
    }

    /**
     * Get the total amount of kills of the player ffa data.
     *
     * @return The total amount of kills
     */
    public int getTotalFFAKills() {
        return this.ffaData.values().stream().mapToInt(ProfileFFAData::getKills).sum();
    }

    /**
     * Get the total amount of deaths of the player ffa data.
     *
     * @return The total amount of deaths
     */
    public int getTotalFFADeaths() {
        return this.ffaData.values().stream().mapToInt(ProfileFFAData::getDeaths).sum();
    }

    public void incrementUnrankedWins() {
        this.unrankedWins++;
    }

    public void incrementUnrankedLosses() {
        this.unrankedLosses++;
    }

    public void incrementRankedWins() {
        this.rankedWins++;
    }

    public void incrementRankedLosses() {
        this.rankedLosses++;
    }
}