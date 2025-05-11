//package dev.revere.alley.essential.parkour;
//
//import dev.revere.alley.Alley;
//import dev.revere.alley.essential.parkour.data.ParkourData;
//import dev.revere.alley.profile.Profile;
//import dev.revere.alley.profile.enums.EnumProfileState;
//import dev.revere.alley.tool.logger.Logger;
//import dev.revere.alley.util.chat.CC;
//import dev.revere.alley.util.location.LocationUtil;
//import lombok.Getter;
//import org.bukkit.Location;
//import org.bukkit.configuration.file.FileConfiguration;
//import org.bukkit.entity.Player;
//
//import java.util.HashMap;
//import java.util.Map;
//
/// **
// * @author Emmy
// * @project Alley
// * @since 25/04/2025
// */
//@Getter
//public class ParkourService {
//    protected final Alley plugin;
//
//    private final Map<ParkourData, Player> players;
//
//    private Location starterLocation;
//
//    /**
//     * Constructor for the ParkourService class.
//     *
//     * @param plugin          The Alley plugin instance.
//     * @param starterLocation The starting location for parkour.
//     */
//    public ParkourService(Alley plugin, String starterLocation) {
//        this.plugin = plugin;
//
//        this.players = new HashMap<>();
//
//        try {
//            this.starterLocation = LocationUtil.deserialize(starterLocation);
//        } catch (Exception exception) {
//            this.starterLocation = plugin.getSpawnService().getLocation();
//            Logger.logError("Parkour starter location is null. Using spawn location instead.");
//        }
//    }
//
//    /**
//     * Sets the starter location for parkour and saves it in the settings.yml file.
//     *
//     * @param location The location to set as the starter location.
//     */
//    public void setStarterLocation(Location location) {
//        this.starterLocation = location;
//
//        FileConfiguration config = this.plugin.getConfigService().getSettingsConfig();
//
//        config.set("parkour.starter-location", LocationUtil.serialize(location));
//        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("settings.yml"), config);
//    }
//
//    /**
//     * Adds a player to the parkour.
//     *
//     * @param player The player to add.
//     */
//    public void addPlayer(Player player) {
//        if (this.starterLocation == null) {
//            player.sendMessage(CC.translate("&cParkour spawn isn't set. Not ready to be played."));
//            return;
//        }
//
//        Profile profile = this.getProfile(player);
//        if (profile.getState() != EnumProfileState.WAITING) {
//            player.sendMessage(CC.translate("&cParkour is only playable during queue."));
//            return;
//        }
//
//        if (this.players.containsValue(player)) {
//            player.sendMessage(CC.translate("&cYou are already in a parkour."));
//            return;
//        }
//
//        ParkourData parkourData = new ParkourData();
//        this.players.put(parkourData, player);
//
//        player.teleport(this.starterLocation);
//
//        player.sendMessage(CC.translate("&bJoined Parkour!"));
//        player.sendMessage(CC.translate("&cPlayable until you leave or the queue finds an opponent."));
//    }
//
//    /**
//     * Removes a player from the parkour.
//     *
//     * @param player The player to remove.
//     * @param notify Whether to notify the player.
//     */
//    public void removePlayer(Player player, boolean notify) {
//        ParkourData parkourData = this.getParkourData(player);
//        if (parkourData == null) {
//            player.sendMessage(CC.translate("&cYou are not in a parkour."));
//            return;
//        }
//
//        this.players.remove(parkourData);
//
//        player.teleport(this.plugin.getSpawnService().getLocation());
//        if (notify) {
//            player.sendMessage(CC.translate("&bLeft Parkour!"));
//        }
//    }
//
//    /**
//     * Checks if a player is currently playing parkour.
//     *
//     * @param player The player to check.
//     * @return true if the player is playing parkour, false otherwise.
//     */
//    public boolean isPlaying(Player player) {
//        return this.getPlayers().containsValue(player) && this.getParkourData(player) != null;
//    }
//
//    /**
//     * Retrieves the profile of a player.
//     *
//     * @param player The player whose profile to retrieve.
//     * @return The player's profile.
//     */
//    private Profile getProfile(Player player) {
//        return this.plugin.getProfileService().getProfile(player.getUniqueId());
//    }
//
//    /**
//     * Retrieves the ParkourData associated with a player.
//     *
//     * @param player The player whose ParkourData to retrieve.
//     * @return The ParkourData associated with the player, or null if not found.
//     */
//    public ParkourData getParkourData(Player player) {
//        for (Map.Entry<ParkourData, Player> entry : this.players.entrySet()) {
//            if (entry.getValue().equals(player)) {
//                return entry.getKey();
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Method to remove players from parkour if they are playing.
//     *
//     * @param player    The player to check.
//     * @param tpToSpawn Whether to teleport to spawn.
//     */
//    public void removeIfPlayingParkour(Player player, boolean tpToSpawn) {
//        if (this.isPlaying(player)) {
//            this.removePlayer(player, false);
//        }
//
//        if (tpToSpawn) {
//            this.plugin.getSpawnService().teleportToSpawn(player);
//        }
//    }
//}