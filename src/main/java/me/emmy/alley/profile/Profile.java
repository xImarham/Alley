package me.emmy.alley.profile;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.game.ffa.AbstractFFAMatch;
import me.emmy.alley.visual.leaderboard.menu.leaderboard.enums.EnumLeaderboardType;
import me.emmy.alley.game.match.AbstractMatch;
import me.emmy.alley.party.Party;
import me.emmy.alley.profile.data.ProfileData;
import me.emmy.alley.profile.enums.EnumProfileState;
import me.emmy.alley.queue.QueueProfile;
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
    public static final int DEFAULT_COINS = 100;
    public static final int DEFAULT_ELO = 1000;
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
