package dev.revere.alley.profile;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.feature.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.feature.queue.QueueProfile;
import dev.revere.alley.feature.queue.enums.EnumQueueType;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.profile.data.impl.ProfileFFAData;
import dev.revere.alley.profile.data.impl.ProfileRankedKitData;
import dev.revere.alley.profile.data.impl.ProfileUnrankedKitData;
import dev.revere.alley.profile.enums.EnumProfileState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 22:35
 */
@Getter
@Setter
public class Profile {
    private final UUID uuid;
    private String name;

    private ProfileData profileData;
    private QueueProfile queueProfile;
    private EnumProfileState state;

    private EnumLeaderboardType leaderboardType;
    private EnumQueueType queueType;

    private AbstractFFAMatch ffaMatch;
    private AbstractMatch match;
    private Party party;

    private boolean online;

    /**
     * Constructor for the Profile class.
     *
     * @param uuid The UUID of the player.
     */
    public Profile(UUID uuid) {
        this.uuid = uuid;
        this.state = EnumProfileState.LOBBY;
        this.profileData = new ProfileData();
        this.name = Bukkit.getOfflinePlayer(this.uuid).getName();
        this.leaderboardType = EnumLeaderboardType.RANKED;
        this.queueType = EnumQueueType.UNRANKED;
    }

    /**
     * Loads the profile from the database.
     */
    public void load() {
        Alley.getInstance().getProfileService().getIProfile().loadProfile(this);
    }

    /**
     * Saves the profile to the database.
     */
    public void save() {
        Alley.getInstance().getProfileService().getIProfile().saveProfile(this);
    }

    /**
     * Retrieves a sorted list of kits that the profile has participated in
     * based on the profile's ELO for each kit, overall wins/losses and FFA kills/deaths.
     *
     * @return A sorted list of kits that the profile has participated in.
     */
    public List<Kit> getSortedKits() {
        return Alley.getInstance().getKitService().getKits()
                .stream()
                .filter(kit -> {
                    ProfileRankedKitData rankedData = this.profileData.getRankedKitData().get(kit.getName());
                    ProfileUnrankedKitData unrankedData = this.profileData.getUnrankedKitData().get(kit.getName());
                    ProfileFFAData ffaData = this.profileData.getFfaData().get(kit.getName());

                    return (rankedData != null && (rankedData.getWins() != 0 || rankedData.getLosses() != 0)) ||
                            (unrankedData != null && (unrankedData.getWins() != 0 || unrankedData.getLosses() != 0)) ||
                            (ffaData != null && (ffaData.getKills() != 0 || ffaData.getDeaths() != 0));
                })
                .sorted(Comparator.comparingInt((Kit kit) -> {
                            ProfileRankedKitData ranked = this.profileData.getRankedKitData().get(kit.getName());
                            return ranked != null ? ranked.getElo() : 0;
                        }).reversed()
                        .thenComparingInt(kit -> {
                            ProfileRankedKitData ranked = this.profileData.getRankedKitData().get(kit.getName());
                            return ranked != null ? ranked.getWins() : 0;
                        }).reversed()
                        .thenComparingInt(kit -> {
                            ProfileFFAData ffa = this.profileData.getFfaData().get(kit.getName());
                            return ffa != null ? ffa.getKills() : 0;
                        }).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Checks if the profile has participated in ranked matches.
     *
     * @return True if the profile has participated in ranked matches, otherwise false.
     */
    public boolean hasParticipatedInRanked() {
        return this.profileData.getRankedKitData().values().stream().anyMatch(data -> data.getWins() > 0 || data.getLosses() > 0 || data.getElo() != 1000);
    }

    /**
     * Checks if the profile has participated in unranked matches.
     *
     * @return True if the profile has participated in unranked matches, otherwise false.
     */
    public boolean hasParticipatedInTournament() {
        return false; //TODO: Implement tournament system
    }

    /**
     * Checks if the profile has participated in FFA matches.
     *
     * @return True if the profile has participated in FFA matches, otherwise false.
     */
    public boolean hasParticipatedInFFA() {
        return this.profileData.getFfaData().values().stream().anyMatch(data -> data.getKills() > 0 || data.getDeaths() > 0);
    }
}