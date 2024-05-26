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
        document.put("name", profile.getName());

        //stats object
        Document statsDocument = new Document();
        statsDocument.put("coins", profile.getProfileData().getCoins());
        statsDocument.put("unrankedWins", profile.getProfileData().getUnrankedWins());
        statsDocument.put("unrankedLosses", profile.getProfileData().getUnrankedLosses());
        statsDocument.put("rankedWins", profile.getProfileData().getRankedWins());
        statsDocument.put("rankedLosses", profile.getProfileData().getRankedLosses());
        statsDocument.put("ffaWins", profile.getProfileData().getFfaWins());
        statsDocument.put("ffaDeaths", profile.getProfileData().getFfaDeaths());
        document.append("stats", statsDocument);

        //settings object
        Document optionsDocument = new Document();
        optionsDocument.put("scoreboardEnabled", profile.getProfileData().getPlayerSettings().isScoreboardEnabled());
        optionsDocument.put("tablistEnabled", profile.getProfileData().getPlayerSettings().isTablistEnabled());
        optionsDocument.put("partyInvitesEnabled", profile.getProfileData().getPlayerSettings().isPartyInvitesEnabled());
        optionsDocument.put("partyMessagesEnabled", profile.getProfileData().getPlayerSettings().isPartyMessagesEnabled());
        document.append("options", optionsDocument);

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

        // Statistics
        if (!document.containsKey("stats")) {
            saveProfile(profile);
            return;
        }

        Document statsDocument = (Document) document.get("stats");
        if (statsDocument.containsKey("coins")) {
            profile.getProfileData().setCoins(statsDocument.getInteger("coins"));
        }

        if (statsDocument.containsKey("unrankedWins")) {
            profile.getProfileData().setUnrankedWins(statsDocument.getInteger("unrankedWins"));
        }

        if (statsDocument.containsKey("unrankedLosses")) {
            profile.getProfileData().setUnrankedLosses(statsDocument.getInteger("unrankedLosses"));
        }

        if (statsDocument.containsKey("rankedWins")) {
            profile.getProfileData().setRankedWins(statsDocument.getInteger("rankedWins"));
        }

        if (statsDocument.containsKey("rankedLosses")) {
            profile.getProfileData().setRankedLosses(statsDocument.getInteger("rankedLosses"));
        }

        if (statsDocument.containsKey("ffaWins")) {
            profile.getProfileData().setFfaWins(statsDocument.getInteger("ffaWins"));
        }

        if (statsDocument.containsKey("ffaDeaths")) {
            profile.getProfileData().setFfaDeaths(statsDocument.getInteger("ffaDeaths"));
        }

        // Player Settings
        if (!document.containsKey("options")) {
            saveProfile(profile);
            return;
        }

        Document options = (Document) document.get("options");
        if (options.containsKey("scoreboardEnabled")) {
            profile.getProfileData().getPlayerSettings().setScoreboardEnabled(options.getBoolean("scoreboardEnabled"));
        }

        if (options.containsKey("tablistEnabled")) {
            profile.getProfileData().getPlayerSettings().setTablistEnabled(options.getBoolean("tablistEnabled"));
        }

        if (options.containsKey("partyInvitesEnabled")) {
            profile.getProfileData().getPlayerSettings().setPartyInvitesEnabled(options.getBoolean("partyInvitesEnabled"));
        }

        if (options.containsKey("partyMessagesEnabled")) {
            profile.getProfileData().getPlayerSettings().setPartyMessagesEnabled(options.getBoolean("partyMessagesEnabled"));
        }

    }
}
