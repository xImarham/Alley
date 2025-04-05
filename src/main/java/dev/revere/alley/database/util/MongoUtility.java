package dev.revere.alley.database.util;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.division.Division;
import dev.revere.alley.feature.division.tier.DivisionTier;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.profile.data.impl.*;
import dev.revere.alley.profile.enums.EnumChatChannel;
import dev.revere.alley.profile.enums.EnumWorldTime;
import lombok.experimental.UtilityClass;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@UtilityClass
public class MongoUtility {
    /**
     * Converts a Profile object to a Document.
     *
     * @param profile The profile to convert.
     * @return The converted Document.
     */
    public Document toDocument(Profile profile) {
        Document document = new Document();
        document.put("firstJoin", profile.getFirstJoin());
        document.put("uuid", profile.getUuid().toString());
        document.put("name", profile.getName());

        Document profileDataDocument = new Document();
        ProfileData profileData = profile.getProfileData();

        profileDataDocument.put("elo", profileData.getElo());
        profileDataDocument.put("coins", profileData.getCoins());
        profileDataDocument.put("unrankedWins", profileData.getUnrankedWins());
        profileDataDocument.put("unrankedLosses", profileData.getUnrankedLosses());
        profileDataDocument.put("rankedWins", profileData.getRankedWins());
        profileDataDocument.put("rankedLosses", profileData.getRankedLosses());

        profileDataDocument.put("rankedBanned", profileData.isRankedBanned());

        profileDataDocument.put("unrankedKitData", convertUnrankedKitData(profileData.getUnrankedKitData()));
        profileDataDocument.put("rankedKitData", convertRankedKitData(profileData.getRankedKitData()));
        profileDataDocument.put("ffaData", convertFFAData(profileData.getFfaData()));
        profileDataDocument.put("profileSettingData", convertProfileSettingData(profileData.getProfileSettingData()));
        profileDataDocument.put("profileCosmeticData", convertProfileCosmeticData(profileData.getProfileCosmeticData()));
        profileDataDocument.put("profilePlayTimeData", convertProfilePlayTimeData(profileData.getProfilePlayTimeData()));

        document.put("profileData", profileDataDocument);
        return document;
    }

    /**
     * Converts a Map of ProfileUnrankedKitData objects to a Document.
     *
     * @param kitData The kit data to convert.
     * @return The converted Document.
     */
    private Document convertUnrankedKitData(Map<String, ProfileUnrankedKitData> kitData) {
        Document kitDataDocument = new Document();
        for (Map.Entry<String, ProfileUnrankedKitData> entry : kitData.entrySet()) {
            Document kitEntry = new Document();
            kitEntry.put("division", entry.getValue().getDivision().getName());
            kitEntry.put("tier", entry.getValue().getTier().getName());
            kitEntry.put("wins", entry.getValue().getWins());
            kitEntry.put("losses", entry.getValue().getLosses());
            kitDataDocument.put(entry.getKey(), kitEntry);
        }
        return kitDataDocument;
    }

    /**
     * Converts a Map of ProfileKitData objects to a Document.
     *
     * @param kitData The kit data to convert.
     * @return The converted Document.
     */
    private Document convertRankedKitData(Map<String, ProfileRankedKitData> kitData) {
        Document kitDataDocument = new Document();
        for (Map.Entry<String, ProfileRankedKitData> entry : kitData.entrySet()) {
            Document kitEntry = new Document();
            kitEntry.put("elo", entry.getValue().getElo());
            kitEntry.put("wins", entry.getValue().getWins());
            kitEntry.put("losses", entry.getValue().getLosses());
            kitDataDocument.put(entry.getKey(), kitEntry);
        }
        return kitDataDocument;
    }

    /**
     * Converts a Map of ProfileFFAData objects to a Document.
     *
     * @param ffaData The FFA data to convert.
     * @return The converted Document.
     */
    private Document convertFFAData(Map<String, ProfileFFAData> ffaData) {
        Document ffaDataDocument = new Document();
        for (Map.Entry<String, ProfileFFAData> entry : ffaData.entrySet()) {
            Document ffaEntry = new Document();
            ffaEntry.put("kills", entry.getValue().getKills());
            ffaEntry.put("deaths", entry.getValue().getDeaths());
            ffaDataDocument.put(entry.getKey(), ffaEntry);
        }
        return ffaDataDocument;
    }

    /**
     * Converts a ProfileSettingData object to a Document.
     *
     * @param settingData The setting data to convert.
     * @return The converted Document.
     */
    private Document convertProfileSettingData(ProfileSettingData settingData) {
        Document settingDocument = new Document();
        settingDocument.put("partyMessagesEnabled", settingData.isPartyMessagesEnabled());
        settingDocument.put("partyInvitesEnabled", settingData.isPartyInvitesEnabled());
        settingDocument.put("scoreboardEnabled", settingData.isScoreboardEnabled());
        settingDocument.put("tablistEnabled", settingData.isTablistEnabled());
        settingDocument.put("showScoreboardLines", settingData.isShowScoreboardLines());
        settingDocument.put("chatChannel", settingData.getChatChannel());
        settingDocument.put("time", settingData.getTime());
        return settingDocument;
    }

    /**
     * Converts a ProfileCosmeticData object to a Document.
     *
     * @param cosmeticData The cosmetic data to convert.
     * @return The converted Document.
     */
    private Document convertProfileCosmeticData(ProfileCosmeticData cosmeticData) {
        Document cosmeticDocument = new Document();
        cosmeticDocument.put("selectedKillEffect", cosmeticData.getSelectedKillEffect());
        cosmeticDocument.put("selectedSoundEffect", cosmeticData.getSelectedSoundEffect());
        return cosmeticDocument;
    }

    /**
     * Converts a ProfilePlayTimeData object to a Document.
     *
     * @param playTimeData The play time data to convert.
     * @return The converted Document.
     */
    private Document convertProfilePlayTimeData(ProfilePlayTimeData playTimeData) {
        Document playTimeDocument = new Document();
        playTimeDocument.put("total", playTimeData.getTotal());
        playTimeDocument.put("lastLogin", playTimeData.getLastLogin());
        return playTimeDocument;
    }

    /**
     * Updates a Profile object from a Document.
     *
     * @param profile   The profile to update.
     * @param document  The document to update from.
     */
    public void updateProfileFromDocument(Profile profile, Document document) {
        profile.setFirstJoin(document.getLong("firstJoin"));

        if (document.containsKey("profileData")) {
            Document profileDataDocument = (Document) document.get("profileData");
            ProfileData profileData = new ProfileData();

            profileData.setElo(profileDataDocument.getInteger("elo"));
            profileData.setCoins(profileDataDocument.getInteger("coins"));
            profileData.setUnrankedWins(profileDataDocument.getInteger("unrankedWins"));
            profileData.setUnrankedLosses(profileDataDocument.getInteger("unrankedLosses"));
            profileData.setRankedWins(profileDataDocument.getInteger("rankedWins"));
            profileData.setRankedLosses(profileDataDocument.getInteger("rankedLosses"));

            profileData.setRankedBanned(profileDataDocument.getBoolean("rankedBanned"));

            Map<String, ProfileUnrankedKitData> existingUnrankedKitData = profileData.getUnrankedKitData();
            Map<String, ProfileUnrankedKitData> newUnrankedKitData = parseUnrankedKitData((Document) profileDataDocument.get("unrankedKitData"));
            existingUnrankedKitData.putAll(newUnrankedKitData);
            profileData.setUnrankedKitData(existingUnrankedKitData);

            Map<String, ProfileRankedKitData> existingRankedKitData = profileData.getRankedKitData();
            Map<String, ProfileRankedKitData> newKitData = parseRankedKitData((Document) profileDataDocument.get("rankedKitData"));
            existingRankedKitData.putAll(newKitData);
            profileData.setRankedKitData(existingRankedKitData);

            Map<String, ProfileFFAData> existingFFAData = profileData.getFfaData();
            Map<String, ProfileFFAData> newFFAData = parseFFAData((Document) profileDataDocument.get("ffaData"));
            existingFFAData.putAll(newFFAData);
            profileData.setFfaData(existingFFAData);

            profileData.setProfileSettingData(parseProfileSettingData((Document) profileDataDocument.get("profileSettingData")));
            profileData.setProfileCosmeticData(parseProfileCosmeticData((Document) profileDataDocument.get("profileCosmeticData")));
            profileData.setProfilePlayTimeData(parseProfilePlayTimeData((Document) profileDataDocument.get("profilePlayTimeData")));

            profile.setProfileData(profileData);
        }
    }

    /**
     * Parses a Map of ProfileUnrankedKitData objects from a Document.
     *
     * @param kitDataDocument The kit data document to parse.
     * @return The parsed Map.
     */
    private Map<String, ProfileUnrankedKitData> parseUnrankedKitData(Document kitDataDocument) {
        Map<String, ProfileUnrankedKitData> kitData = new HashMap<>();
        for (Map.Entry<String, Object> entry : kitDataDocument.entrySet()) {
            Document kitEntry = (Document) entry.getValue();
            ProfileUnrankedKitData kit = new ProfileUnrankedKitData();

            String storedDivision = kitEntry.getString("division");
            Division division = Alley.getInstance().getDivisionService().getDivision(storedDivision);
            kit.setDivision(division.getName());

            String storedTier = kitEntry.getString("tier");
            DivisionTier tier = division.getTiers().stream()
                    .filter(t -> t.getName().equals(storedTier))
                    .findFirst()
                    .orElse(null);
            kit.setTier(Objects.requireNonNull(tier).getName());

            kit.setWins(kitEntry.getInteger("wins"));
            kit.setLosses(kitEntry.getInteger("losses"));

            kitData.put(entry.getKey(), kit);
        }
        return kitData;
    }


    /**
     * Parses a Map of ProfileKitData objects from a Document.
     *
     * @param kitDataDocument The kit data document to parse.
     * @return The parsed Map.
     */
    private Map<String, ProfileRankedKitData> parseRankedKitData(Document kitDataDocument) {
        Map<String, ProfileRankedKitData> kitData = new HashMap<>();
        for (Map.Entry<String, Object> entry : kitDataDocument.entrySet()) {
            Document kitEntry = (Document) entry.getValue();
            ProfileRankedKitData kit = new ProfileRankedKitData();
            kit.setElo(kitEntry.getInteger("elo"));
            kit.setWins(kitEntry.getInteger("wins"));
            kit.setLosses(kitEntry.getInteger("losses"));
            kitData.put(entry.getKey(), kit);
        }
        return kitData;
    }

    /**
     * Parses a Map of ProfileFFAData objects from a Document.
     *
     * @param ffaDataDocument The FFA data document to parse.
     * @return The parsed Map.
     */
    private Map<String, ProfileFFAData> parseFFAData(Document ffaDataDocument) {
        Map<String, ProfileFFAData> ffaData = new HashMap<>();
        for (Map.Entry<String, Object> entry : ffaDataDocument.entrySet()) {
            Document ffaEntry = (Document) entry.getValue();
            ProfileFFAData ffa = new ProfileFFAData();
            ffa.setKills(ffaEntry.getInteger("kills"));
            ffa.setDeaths(ffaEntry.getInteger("deaths"));
            ffaData.put(entry.getKey(), ffa);
        }
        return ffaData;
    }

    /**
     * Parses a ProfileSettingData object from a Document.
     *
     * @param settingDocument The setting document to parse.
     * @return The parsed ProfileSettingData.
     */
    private ProfileSettingData parseProfileSettingData(Document settingDocument) {
        ProfileSettingData settingData = new ProfileSettingData();
        settingData.setPartyMessagesEnabled(settingDocument.getBoolean("partyMessagesEnabled", true));
        settingData.setPartyInvitesEnabled(settingDocument.getBoolean("partyInvitesEnabled", true));
        settingData.setScoreboardEnabled(settingDocument.getBoolean("scoreboardEnabled", true));
        settingData.setTablistEnabled(settingDocument.getBoolean("tablistEnabled", true));
        settingData.setShowScoreboardLines(settingDocument.getBoolean("showScoreboardLines", true));
        settingData.setChatChannel(settingDocument.get("chatChannel", EnumChatChannel.GLOBAL.toString()));
        settingData.setTime(settingDocument.get("time", EnumWorldTime.DEFAULT.getName()));
        return settingData;
    }

    /**
     * Parses a ProfileCosmeticData object from a Document.
     *
     * @param cosmeticDocument The cosmetic document to parse.
     * @return The parsed ProfileCosmeticData.
     */
    private ProfileCosmeticData parseProfileCosmeticData(Document cosmeticDocument) {
        ProfileCosmeticData cosmeticData = new ProfileCosmeticData();
        cosmeticData.setSelectedKillEffect(cosmeticDocument.getString("selectedKillEffect"));
        cosmeticData.setSelectedSoundEffect(cosmeticDocument.getString("selectedSoundEffect"));
        return cosmeticData;
    }

    /**
     * Parses a ProfilePlayTimeData object from a Document.
     *
     * @param playTimeDocument The play time document to parse.
     * @return The parsed ProfilePlayTimeData.
     */
    private ProfilePlayTimeData parseProfilePlayTimeData(Document playTimeDocument) {
        ProfilePlayTimeData playTimeData = new ProfilePlayTimeData();
        playTimeData.setTotal(playTimeDocument.getLong("total"));
        playTimeData.setLastLogin(playTimeDocument.getLong("lastLogin"));
        return playTimeData;
    }
}