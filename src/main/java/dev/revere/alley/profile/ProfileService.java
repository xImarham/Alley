package dev.revere.alley.profile;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.UpdateOptions;
import dev.revere.alley.Alley;
import dev.revere.alley.database.profile.IProfile;
import dev.revere.alley.database.profile.impl.MongoProfileImpl;
import dev.revere.alley.database.util.MongoUtility;
import dev.revere.alley.profile.data.ProfileData;
import dev.revere.alley.tool.date.DateFormatter;
import dev.revere.alley.tool.date.enums.EnumDateFormat;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/20/2024
 */
@Getter
public class ProfileService {
    private final HashMap<UUID, Profile> profiles;
    public MongoCollection<Document> collection;
    public IProfile iProfile;

    public ProfileService() {
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

    /**
     * Resets the stats of the target player.
     *
     * @param player The player that issued the stat reset.
     * @param target The UUID of the target player.
     */
    public void resetStats(Player player, UUID target) {
        Profile profile = this.getProfile(target);
        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);

        this.archiveProfile(profile);

        profile.setProfileData(new ProfileData());
        profile.save();

        Arrays.asList(
                "",
                "&c&lSTAT RESET ISSUED",
                "&cSuccessfully reset stats of " + targetPlayer.getName() + ".",
                "&7Be aware that if this is being abused, you will be punished.",
                ""
        ).forEach(line -> player.sendMessage(CC.translate(line)));

        if (targetPlayer.isOnline()) {
            Arrays.asList(
                    "",
                    "&c&lSTAT RESET ACTION",
                    "&cYour stats have been wiped due to suspicious activity.",
                    "&7If you believe this was unjust, create a support ticket.",
                    ""
            ).forEach(line -> targetPlayer.getPlayer().sendMessage(CC.translate(line)));
        }
    }

    /**
     * Archives a player's profile before resetting stats.
     *
     * @param profile The profile to archive.
     */
    public void archiveProfile(Profile profile) {
        Document archiveDocument = new Document();

        DateFormatter formattedDate = new DateFormatter(EnumDateFormat.DATE_PLUS_TIME, System.currentTimeMillis());
        String archiveId = UUID.randomUUID().toString();

        archiveDocument.put("archive_id", archiveId);
        archiveDocument.put("archived_at", formattedDate);
        archiveDocument.put("data", MongoUtility.toDocument(profile));

        Alley.getInstance().getMongoService().getMongoDatabase().getCollection("profile_archives").updateOne(
                new Document("uuid", profile.getUuid().toString()),
                new Document("$push", new Document("archives", archiveDocument)),
                new UpdateOptions().upsert(true)
        );
    }
}