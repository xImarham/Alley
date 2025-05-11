//package dev.revere.alley.essential.parkour.data;
//
//import dev.revere.alley.Alley;
//import lombok.Getter;
//import lombok.Setter;
//import org.bukkit.Location;
//
//import java.util.ArrayList;
//import java.util.List;
//
/// **
// * @author Emmy
// * @project Alley
// * @since 25/04/2025
// */
//@Getter
//@Setter
//public class ParkourData {
//    private List<Location> checkpoints;
//
//    /**
//     * Constructor for the ParkourData class.
//     */
//    public ParkourData() {
//        this.checkpoints = new ArrayList<>();
//        this.checkpoints.add(Alley.getInstance().getParkourService().getStarterLocation());
//    }
//
//    /**
//     * Adds a checkpoint to the list of checkpoints.
//     *
//     * @param location The location to add as a checkpoint.
//     */
//    public void addCheckpoint(Location location) {
//        this.checkpoints.add(location);
//    }
//
//    /**
//     * Checks if the given location is a checkpoint.
//     *
//     * @param location The location to check.
//     * @return true if the location is a checkpoint, false otherwise.
//     */
//    public boolean hasCheckpoint(Location location) {
//        for (Location checkpoint : this.checkpoints) {
//            if (checkpoint.getBlockX() == location.getBlockX()
//                    && checkpoint.getBlockY() == location.getBlockY()
//                    && checkpoint.getBlockZ() == location.getBlockZ()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * Returns the last checkpoint in the list.
//     *
//     * @return The last checkpoint location, or null if there are no checkpoints.
//     */
//    public Location getLastCheckpoint() {
//        if (this.checkpoints.isEmpty()) {
//            return null;
//        }
//        return this.checkpoints.get(this.checkpoints.size() - 1);
//    }
//}