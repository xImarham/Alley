package dev.revere.alley.profile;

import com.mongodb.client.MongoCollection;
import dev.revere.alley.Alley;
import dev.revere.alley.database.profile.IProfile;
import dev.revere.alley.database.profile.impl.MongoProfileImpl;
import lombok.Getter;
import org.bson.Document;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
@Getter
public class ProfileRepository {
    private final HashMap<UUID, Profile> profiles;
    public MongoCollection<Document> collection;
    public IProfile iProfile;

    public ProfileRepository() {
        this.profiles = new HashMap<>();
        this.collection = Alley.getInstance().getMongoService().getMongoDatabase().getCollection("profiles");
        this.iProfile = new MongoProfileImpl();
    }

    /**
     * Gets a profile by UUID.
     *
     * @param uuid The UUID of the profile.
     * @return The profile.
     */
    public Profile getProfile(UUID uuid) {
        if (!this.profiles.containsKey(uuid)) {
            Profile profile = new Profile(uuid);
            profile.load();

            this.profiles.put(uuid, profile);
        }
        return this.profiles.get(uuid);
    }

    /**
     * Loads all profiles from the database.
     */
    public void loadProfiles() {
        for (Document document : this.collection.find()) {
            UUID uuid = UUID.fromString(document.getString("uuid"));
            Profile profile = new Profile(uuid);
            profile.load();

            this.profiles.put(uuid, profile);
        }
    }

    /**
     * Adds a profile to the repository.
     *
     * @param profile The profile to add.
     */
    public void addProfile(Profile profile) {
        this.profiles.put(profile.getUuid(), profile);
    }
}