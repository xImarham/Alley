package me.emmy.alley.profile;

import com.mongodb.client.MongoCollection;
import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.database.profile.IProfile;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

@Getter
@Setter
public class ProfileRepository {

    private final HashMap<UUID, Profile> profiles = new HashMap<>();
    public MongoCollection<Document> collection;
    public IProfile iProfile;

    /**
     * Gets a profile by UUID.
     *
     * @param uuid The UUID of the profile.
     * @return The profile.
     */
    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    /**
     * Loads all profiles from the database.
     */
    public void loadProfiles() {
        this.collection = Alley.getInstance().getMongoService().getMongoDatabase().getCollection("profiles");

        for (Document document : collection.find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            Profile profile = new Profile(uuid);
            profile.load();

            Alley.getInstance().getProfileRepository().getProfiles().put(uuid, profile);
        }
    }
}
