package me.emmy.alley.database.util;

import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.data.ProfileData;
import me.emmy.alley.profile.data.impl.ProfileCosmeticData;
import me.emmy.alley.profile.data.impl.ProfileDivisionData;
import me.emmy.alley.profile.data.impl.ProfileFFAData;
import me.emmy.alley.profile.data.impl.ProfileKitData;
import me.emmy.alley.profile.data.impl.ProfileSettingData;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for converting between Profile objects and MongoDB Documents.
 *
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
public class MongoUtility {

    /**
     * Converts a Profile object to a MongoDB Document manually.
     *
     * @param profile The Profile object to convert.
     * @return The created Document.
     */
    public static Document toDocument(Profile profile) {
        Document document = new Document();
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());

        Document profileDataDocument = new Document();
        ProfileData profileData = profile.getProfileData();

        profileDataDocument.put("coins", profileData.getCoins());
        profileDataDocument.put("unrankedWins", profileData.getUnrankedWins());
        profileDataDocument.put("unrankedLosses", profileData.getUnrankedLosses());
        profileDataDocument.put("rankedWins", profileData.getRankedWins());
        profileDataDocument.put("rankedLosses", profileData.getRankedLosses());

        profileDataDocument.put("kitData", convertKitData(profileData.getKitData()));
        profileDataDocument.put("ffaData", convertFFAData(profileData.getFfaData()));
        profileDataDocument.put("profileSettingData", convertProfileSettingData(profileData.getProfileSettingData()));
        profileDataDocument.put("profileCosmeticData", convertProfileCosmeticData(profileData.getProfileCosmeticData()));
        profileDataDocument.put("profileDivisionData", convertProfileDivisionData(profileData.getProfileDivisionData()));

        document.put("profileData", profileDataDocument);
        return document;
    }

    private static Document convertKitData(Map<String, ProfileKitData> kitData) {
        Document kitDataDocument = new Document();
        for (Map.Entry<String, ProfileKitData> entry : kitData.entrySet()) {
            Document kitEntry = new Document();
            kitEntry.put("elo", entry.getValue().getElo());
            kitEntry.put("wins", entry.getValue().getWins());
            kitEntry.put("losses", entry.getValue().getLosses());
            kitDataDocument.put(entry.getKey(), kitEntry);
        }
        return kitDataDocument;
    }

    private static Document convertFFAData(Map<String, ProfileFFAData> ffaData) {
        Document ffaDataDocument = new Document();
        for (Map.Entry<String, ProfileFFAData> entry : ffaData.entrySet()) {
            Document ffaEntry = new Document();
            ffaEntry.put("kills", entry.getValue().getKills());
            ffaEntry.put("deaths", entry.getValue().getDeaths());
            ffaDataDocument.put(entry.getKey(), ffaEntry);
        }
        return ffaDataDocument;
    }

    private static Document convertProfileSettingData(ProfileSettingData settingData) {
        Document settingDocument = new Document();
        settingDocument.put("scoreboardEnabled", settingData.isScoreboardEnabled());
        settingDocument.put("tablistEnabled", settingData.isTablistEnabled());
        settingDocument.put("partyInvitesEnabled", settingData.isPartyInvitesEnabled());
        settingDocument.put("partyMessagesEnabled", settingData.isPartyMessagesEnabled());
        return settingDocument;
    }

    private static Document convertProfileCosmeticData(ProfileCosmeticData cosmeticData) {
        Document cosmeticDocument = new Document();
        cosmeticDocument.put("selectedKillEffect", cosmeticData.getSelectedKillEffect());
        cosmeticDocument.put("selectedSoundEffect", cosmeticData.getSelectedSoundEffect());
        return cosmeticDocument;
    }

    private static Document convertProfileDivisionData(ProfileDivisionData divisionData) {
        Document divisionDocument = new Document();
        divisionDocument.put("division", divisionData.getDivision());
        divisionDocument.put("globalElo", divisionData.getGlobalElo());
        return divisionDocument;
    }

    /**
     * Updates a Profile object using data from a Document.
     *
     * @param profile The Profile object to update.
     * @param document The Document containing the data.
     */
    public static void updateProfileFromDocument(Profile profile, Document document) {
        if (document.containsKey("profileData")) {
            Document profileDataDocument = (Document) document.get("profileData");
            ProfileData profileData = new ProfileData();

            profileData.setCoins(profileDataDocument.getInteger("coins", Profile.DEFAULT_COINS));
            profileData.setUnrankedWins(profileDataDocument.getInteger("unrankedWins", 0));
            profileData.setUnrankedLosses(profileDataDocument.getInteger("unrankedLosses", 0));
            profileData.setRankedWins(profileDataDocument.getInteger("rankedWins", 0));
            profileData.setRankedLosses(profileDataDocument.getInteger("rankedLosses", 0));

            Map<String, ProfileKitData> existingKitData = profileData.getKitData();
            Map<String, ProfileKitData> newKitData = parseKitData((Document) profileDataDocument.get("kitData"));
            for (Map.Entry<String, ProfileKitData> entry : newKitData.entrySet()) {
                existingKitData.putIfAbsent(entry.getKey(), entry.getValue());
            }
            profileData.setKitData(existingKitData);

            Map<String, ProfileFFAData> existingFFAData = profileData.getFfaData();
            Map<String, ProfileFFAData> newFFAData = parseFFAData((Document) profileDataDocument.get("ffaData"));
            for (Map.Entry<String, ProfileFFAData> entry : newFFAData.entrySet()) {
                existingFFAData.putIfAbsent(entry.getKey(), entry.getValue());
            }
            profileData.setFfaData(existingFFAData);

            profileData.setProfileSettingData(parseProfileSettingData((Document) profileDataDocument.get("profileSettingData")));
            profileData.setProfileCosmeticData(parseProfileCosmeticData((Document) profileDataDocument.get("profileCosmeticData")));
            profileData.setProfileDivisionData(parseProfileDivisionData((Document) profileDataDocument.get("profileDivisionData")));

            profile.setProfileData(profileData);
        }
    }

    private static Map<String, ProfileKitData> parseKitData(Document kitDataDocument) {
        Map<String, ProfileKitData> kitData = new HashMap<>();
        for (Map.Entry<String, Object> entry : kitDataDocument.entrySet()) {
            Document kitEntry = (Document) entry.getValue();
            ProfileKitData kit = new ProfileKitData();
            kit.setElo(kitEntry.getInteger("elo", 1000));
            kit.setWins(kitEntry.getInteger("wins", 0));
            kit.setLosses(kitEntry.getInteger("losses", 0));
            kitData.put(entry.getKey(), kit);
        }
        return kitData;
    }

    private static Map<String, ProfileFFAData> parseFFAData(Document ffaDataDocument) {
        Map<String, ProfileFFAData> ffaData = new HashMap<>();
        for (Map.Entry<String, Object> entry : ffaDataDocument.entrySet()) {
            Document ffaEntry = (Document) entry.getValue();
            ProfileFFAData ffa = new ProfileFFAData();
            ffa.setKills(ffaEntry.getInteger("kills", 0));
            ffa.setDeaths(ffaEntry.getInteger("deaths", 0));
            ffaData.put(entry.getKey(), ffa);
        }
        return ffaData;
    }

    private static ProfileSettingData parseProfileSettingData(Document settingDocument) {
        ProfileSettingData settingData = new ProfileSettingData();
        settingData.setScoreboardEnabled(settingDocument.getBoolean("scoreboardEnabled", true));
        settingData.setTablistEnabled(settingDocument.getBoolean("tablistEnabled", true));
        settingData.setPartyInvitesEnabled(settingDocument.getBoolean("partyInvitesEnabled", true));
        settingData.setPartyMessagesEnabled(settingDocument.getBoolean("partyMessagesEnabled", true));
        return settingData;
    }

    private static ProfileCosmeticData parseProfileCosmeticData(Document cosmeticDocument) {
        ProfileCosmeticData cosmeticData = new ProfileCosmeticData();
        cosmeticData.setSelectedKillEffect(cosmeticDocument.getString("selectedKillEffect"));
        cosmeticData.setSelectedSoundEffect(cosmeticDocument.getString("selectedSoundEffect"));
        return cosmeticData;
    }

    private static ProfileDivisionData parseProfileDivisionData(Document divisionDocument) {
        ProfileDivisionData divisionData = new ProfileDivisionData();
        divisionData.setDivision(divisionDocument.getString("division"));
        divisionData.setGlobalElo(divisionDocument.getInteger("globalElo", Profile.DEFAULT_ELO));
        return divisionData;
    }
}
