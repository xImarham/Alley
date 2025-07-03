package dev.revere.alley.feature.leaderboard;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Projections;
import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.database.IMongoService;
import dev.revere.alley.feature.leaderboard.data.LeaderboardPlayerData;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.feature.leaderboard.record.LeaderboardRecord;
import dev.revere.alley.profile.IProfileService;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.ProfileService;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import dev.revere.alley.profile.data.impl.ProfileUnrankedKitData;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
@Service(provides = ILeaderboardService.class, priority = 280)
public class LeaderboardService implements ILeaderboardService {
    private final IMongoService mongoService;
    private final IKitService kitService;
    private final IProfileService profileService;

    private final Map<Kit, List<LeaderboardRecord>> leaderboardCache = new ConcurrentHashMap<>();

    /**
     * Constructor for DI.
     */
    public LeaderboardService(IMongoService mongoService, IKitService kitService, IProfileService profileService) {
        this.mongoService = mongoService;
        this.kitService = kitService;
        this.profileService = profileService;
    }
    @Override
    public void initialize(AlleyContext context) {
        this.forceRecalculateAll();
    }

    @Override
    public void forceRecalculateAll() {
        MongoCollection<Document> profileCollection = this.mongoService.getMongoDatabase().getCollection("profiles");

        for (Kit kit : this.kitService.getKits()) {
            List<LeaderboardRecord> records = Collections.synchronizedList(new ArrayList<>());
            for (EnumLeaderboardType type : EnumLeaderboardType.values()) {

                List<LeaderboardPlayerData> playerDataList = new ArrayList<>();
                Bson projection = Projections.fields(Projections.include("uuid", "name", "profileData"), Projections.excludeId());

                for (Document doc : profileCollection.find().projection(projection)) {
                    try {
                        Document profileDataDoc = doc.get("profileData", Document.class);
                        if (profileDataDoc == null) continue;

                        int value = extractValueForType(profileDataDoc, kit, type);
                        if (value > 0 || type == EnumLeaderboardType.RANKED) {
                            playerDataList.add(new LeaderboardPlayerData(
                                    doc.getString("name"),
                                    UUID.fromString(doc.getString("uuid")),
                                    kit,
                                    value
                            ));
                        }
                    } catch (Exception ignored) {
                    }
                }

                playerDataList.sort(Comparator.comparingInt(LeaderboardPlayerData::getValue).reversed());
                records.add(new LeaderboardRecord(type, playerDataList));
            }
            this.leaderboardCache.put(kit, records);
        }
    }

    @Override
    public List<LeaderboardPlayerData> getLeaderboardEntries(Kit kit, EnumLeaderboardType type) {
        this.refreshOnlinePlayers(kit, type);

        return this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(record -> record.getType() == type)
                .findFirst()
                .map(LeaderboardRecord::getParticipants)
                .orElse(Collections.emptyList());
    }

    private void refreshOnlinePlayers(Kit kit, EnumLeaderboardType type) {
        LeaderboardRecord record = this.leaderboardCache.getOrDefault(kit, Collections.emptyList())
                .stream()
                .filter(r -> r.getType() == type)
                .findFirst()
                .orElse(null);

        if (record == null) return;

        List<LeaderboardPlayerData> leaderboard = record.getParticipants();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            Profile profile = this.profileService.getProfile(onlinePlayer.getUniqueId());
            updatePlayerInLeaderboard(leaderboard, profile, kit, type);
        }

        leaderboard.sort(Comparator.comparingInt(LeaderboardPlayerData::getValue).reversed());
    }

    private void updatePlayerInLeaderboard(List<LeaderboardPlayerData> leaderboard, Profile profile, Kit kit, EnumLeaderboardType type) {
        int newValue = getValueForType(profile, kit, type);

        Optional<LeaderboardPlayerData> existingData = leaderboard.stream()
                .filter(p -> p.getUuid().equals(profile.getUuid()))
                .findFirst();

        if (existingData.isPresent()) {
            existingData.get().setValue(newValue);
        } else {
            leaderboard.add(new LeaderboardPlayerData(profile.getName(), profile.getUuid(), kit, newValue));
        }
    }

    private int getValueForType(Profile profile, Kit kit, EnumLeaderboardType type) {
        ProfileData data = profile.getProfileData();
        switch (type) {
            case RANKED:
                return data.getRankedKitData().getOrDefault(kit.getName(), new ProfileRankedKitData()).getElo();
            case UNRANKED:
                return data.getUnrankedKitData().getOrDefault(kit.getName(), new ProfileUnrankedKitData()).getWins();
            case FFA:
                return data.getFfaData().getOrDefault(kit.getName(), new ProfileFFAData()).getKills();
            case WIN_STREAK:
                // todo: smth like return data.getWinStreakData()...?
            default:
                return 0;
        }
    }

    private int extractValueForType(Document profileDataDoc, Kit kit, EnumLeaderboardType type) {
        try {
            switch (type) {
                case RANKED:
                    return Optional.ofNullable(profileDataDoc.get("rankedKitData", Document.class))
                            .map(doc -> doc.get(kit.getName(), Document.class))
                            .map(doc -> doc.getInteger("elo", 1000))
                            .orElse(1000);
                case UNRANKED:
                    return Optional.ofNullable(profileDataDoc.get("unrankedKitData", Document.class))
                            .map(doc -> doc.get(kit.getName(), Document.class))
                            .map(doc -> doc.getInteger("wins", 0))
                            .orElse(0);
                case FFA:
                    return Optional.ofNullable(profileDataDoc.get("ffaData", Document.class))
                            .map(doc -> doc.get(kit.getName(), Document.class))
                            .map(doc -> doc.getInteger("kills", 0))
                            .orElse(0);
                case WIN_STREAK:
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
