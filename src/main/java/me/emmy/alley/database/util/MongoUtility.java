package me.emmy.alley.database.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.ProfileData;
import org.bson.Document;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MongoUtility {
    private static final Gson gson = new GsonBuilder().create();

    /**
     * Converts a Profile object to a Document.
     *
     * @param profile The Profile object to convert.
     * @return The converted Document.
     */
    public static Document toDocument(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());

        Document profileDataDocument = toDocument(profile.getProfileData());
        document.append("profileData", profileDataDocument);

        return document;
    }

    /**
     * Converts a ProfileData object to a Document.
     *
     * @param profileData The ProfileData object to convert.
     * @return The converted Document.
     */
    public static Document toDocument(ProfileData profileData) {
        return Document.parse(gson.toJson(profileData));
    }

    /**
     * Converts a Document to a ProfileData object.
     *
     * @param document The Document to convert.
     * @return The converted ProfileData object.
     */
    public static ProfileData fromProfileDataDocument(Document document) {
        return gson.fromJson(document.toJson(), ProfileData.class);
    }

    /**
     * Updates a Profile object from a Document.
     *
     * @param profile The Profile object to update.
     * @param document The Document to update from.
     */
    public static void updateProfileFromDocument(Profile profile, Document document) {
        if (document.containsKey("profileData")) {
            Document profileDataDocument = (Document) document.get("profileData");
            ProfileData profileData = fromProfileDataDocument(profileDataDocument);
            profile.setProfileData(profileData);
        }
    }
}