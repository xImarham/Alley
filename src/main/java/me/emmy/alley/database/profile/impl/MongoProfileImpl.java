package me.emmy.alley.database.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import me.emmy.alley.Alley;
import me.emmy.alley.database.profile.IProfile;
import me.emmy.alley.profile.Profile;
import org.bson.Document;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public class MongoProfileImpl implements IProfile {
    @Override
    public void saveProfile(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("coins", profile.getProfileData().getCoins());

        Alley.getInstance().getProfileRepository().getCollection().replaceOne(Filters.eq("uuid", profile.getUuid().toString()), document, new ReplaceOptions().upsert(true));
    }

    @Override
    public void loadProfile(Profile profile) {
        if (profile.getUuid() == null) return;
        if (Alley.getInstance().getProfileRepository().getCollection() == null) return;

        Document document = Alley.getInstance().getProfileRepository().getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            saveProfile(profile);
            return;
        }

        if (document.containsKey("coins")) {
            profile.getProfileData().setCoins(document.getInteger("coins"));
        }

    }
}
