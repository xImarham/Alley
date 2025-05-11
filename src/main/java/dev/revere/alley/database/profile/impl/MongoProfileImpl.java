package dev.revere.alley.database.profile.impl;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.UpdateOptions;
import dev.revere.alley.Alley;
import dev.revere.alley.database.profile.IProfile;
import dev.revere.alley.database.util.MongoUtility;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.date.DateFormatter;
import dev.revere.alley.tool.date.enums.EnumDateFormat;
import org.bson.Document;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/22/2024
 */
public class MongoProfileImpl implements IProfile {
    protected final Alley plugin;

    /**
     * Constructor for the MongoProfileImpl class.
     *
     * @param plugin The Alley plugin instance.
     */
    public MongoProfileImpl(Alley plugin) {
        this.plugin = plugin;
    }

    /**
     * Saves a profile to the database.
     *
     * @param profile The profile to save.
     */
    @Override
    public void saveProfile(Profile profile) {
        Document document = MongoUtility.toDocument(profile);
        this.plugin.getProfileService().getCollection()
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
        if (this.plugin.getProfileService().getCollection() == null) return;

        Document document = this.plugin.getProfileService().getCollection().find(Filters.eq("uuid", profile.getUuid().toString())).first();
        if (document == null) {
            this.saveProfile(profile);
            return;
        }

        MongoUtility.updateProfileFromDocument(profile, document);
    }

    /**
     * Archives a profile in the database.
     *
     * @param profile The profile to archive.
     */
    @Override
    public void archiveProfile(Profile profile) {
        Document archiveDocument = new Document();

        DateFormatter dateFormatter = new DateFormatter(EnumDateFormat.DATE_PLUS_TIME, System.currentTimeMillis());
        String archiveId = UUID.randomUUID().toString();

        archiveDocument.put("archive_id", archiveId);
        archiveDocument.put("archived_at", dateFormatter.getDateFormat().format(dateFormatter.getDate()));
        archiveDocument.put("data", MongoUtility.toDocument(profile));

        this.plugin.getMongoService().getMongoDatabase().getCollection("profile_archives").updateOne(
                new Document("uuid", profile.getUuid().toString()),
                new Document("$push", new Document("archives", archiveDocument)),
                new UpdateOptions().upsert(true)
        );
    }
}