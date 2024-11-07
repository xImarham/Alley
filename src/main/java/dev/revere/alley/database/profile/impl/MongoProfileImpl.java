package dev.revere.alley.database.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import dev.revere.alley.Alley;
import dev.revere.alley.database.profile.IProfile;
import dev.revere.alley.database.util.MongoUtility;
import dev.revere.alley.profile.Profile;
import org.bson.Document;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public class MongoProfileImpl implements IProfile {
    /**
     * Saves a profile to the database.
     *
     * @param profile The profile to save.
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = MongoUtility.toDocument(profile);
        Alley.getInstance().getProfileRepository().getCollection()
                .replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Loads a profile from the database.
     *
     * @param profile The profile to load.
     */
    @Override
    public void loadProfile(Profile profile) {
        if (profile.getUuid() == null) return;
        if (Alley.getInstance().getProfileRepository().getCollection() == null) return;

        Document document = Alley.getInstance().getProfileRepository().getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            saveProfile(profile);
            return;
        }

        MongoUtility.updateProfileFromDocument(profile, document);
    }
}