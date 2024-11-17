package dev.revere.alley.profile;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import dev.revere.alley.leaderboard.enums.EnumLeaderboardType;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.party.Party;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.queue.QueueProfile;
import org.bukkit.Bukkit;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 22:35
 */
@Getter
@Setter
public class Profile {
    private EnumLeaderboardType leaderboardType = EnumLeaderboardType.RANKED;
    private QueueProfile queueProfile;
    private AbstractFFAMatch ffaMatch;
    private ProfileData profileData;
    private EnumProfileState state;
    private AbstractMatch match;
    private final UUID uuid;
    private boolean online;
    private String name;
    private Party party;

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
    }

    /**
     * Loads the profile from the database.
     */
    public void load() {
        Alley.getInstance().getProfileRepository().getIProfile().loadProfile(this);
    }

    /**
     * Saves the profile to the database.
     */
    public void save() {
        Alley.getInstance().getProfileRepository().getIProfile().saveProfile(this);
    }
}