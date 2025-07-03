package dev.revere.alley.feature.leaderboard;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.feature.leaderboard.record.LeaderboardRecord;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;

import java.util.*;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
@Service(provides = ILeaderboardService.class, priority = 280)
public class LeaderboardService implements ILeaderboardService {
    private final IProfileService profileService;
    private final IKitService kitService;

    private final Map<Kit, List<LeaderboardRecord>> leaderboardEntries = new HashMap<>();

    /**
     * Constructor for DI.
     */
    public LeaderboardService(IProfileService profileService, IKitService kitService) {
        this.profileService = profileService;
        this.kitService = kitService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.recalculateLeaderboards();
    }

    @Override
    public void recalculateLeaderboards() {
        this.leaderboardEntries.clear();
        Collection<Profile> profiles = this.profileService.getProfiles().values();
        if (profiles.isEmpty()) {
            Logger.warn("LeaderboardService: No profiles found to calculate leaderboards.");
            return;
        }

        for (Kit kit : this.kitService.getKits()) {
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
        Logger.info("Calculated leaderboards for " + this.leaderboardEntries.size() + " kits.");
    }

    @Override
    public List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, EnumLeaderboardType type) {
        return this.leaderboardEntries.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(record -> record.getType() == type)
                .findFirst()
                .map(LeaderboardRecord::getParticipants)
                .orElse(Collections.emptyList());
    }
}
