package dev.revere.alley.feature.leaderboard;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.feature.leaderboard.record.LeaderboardRecord;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import lombok.Getter;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
public class LeaderboardService {

    //TODO: this is just a test, it is gonna be fixed/recoded.

    protected final Alley plugin;
    private final Map<Kit, List<LeaderboardRecord>> leaderboardEntries;

    /**
     * Constructor for the LeaderboardService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public LeaderboardService(Alley plugin) {
        this.plugin = plugin;
        this.leaderboardEntries = new HashMap<>();
        this.initializeLeaderboards();
    }

    private void initializeLeaderboards() {
        ProfileService profileService = this.plugin.getProfileService();
        Collection<Profile> profiles = profileService.getProfiles().values();

        for (Kit kit : this.plugin.getKitService().getKits()) {
            this.leaderboardEntries.put(kit, new ArrayList<>());

            for (EnumLeaderboardType type : EnumLeaderboardType.values()) {
                List<LeaderboardPlayerData> playerDataList = new ArrayList<>();

                for (Profile profile : profiles) {
                    ProfileRankedKitData rankedData = profile.getProfileData().getRankedKitData().get(kit.getName());
                    if (rankedData != null) {
                        playerDataList.add(new LeaderboardPlayerData(
                                profile.getName(),
                                profile.getUuid(),
                                rankedData.getElo(),
                                kit
                        ));
                    }
                }

                playerDataList.sort(Comparator.comparingInt(LeaderboardPlayerData::getElo).reversed());
                this.leaderboardEntries.get(kit).add(new LeaderboardRecord(type, playerDataList));
            }
        }
    }

    /**
     * Gets the leaderboard entries for a specific kit and type.
     *
     * @param kit  The kit to get leaderboard entries for.
     * @param type The leaderboard type.
     * @return A list of LeaderboardPlayerData sorted accordingly.
     */
    public List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, EnumLeaderboardType type) {
        return this.leaderboardEntries.getOrDefault(kit, new ArrayList<>())
                .stream()
                .filter(record -> record.getType() == type)
                .findFirst()
                .map(LeaderboardRecord::getParticipants)
                .orElse(new ArrayList<>());
    }
}
