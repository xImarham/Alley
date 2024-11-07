package dev.revere.alley.util.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.StringJoiner;

@Getter
@Setter
@AllArgsConstructor
public class CustomLocation {
    private String world;

    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    /**
     * Constructor for the CustomLocation class.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     */
    public CustomLocation(double x, double y, double z) {
        this(x, y, z, 0.0F, 0.0F);
    }

    /**
     * Constructor for the CustomLocation class.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @param z The z coordinate.
     * @param yaw The yaw.
     * @param pitch The pitch.
     */
    public CustomLocation(double x, double y, double z, float yaw, float pitch) {
        this("world", x, y, z, yaw, pitch);
    }

    /**
     * Returns a CustomLocation object from a Bukkit Location object.
     *
     * @param location The Bukkit Location object.
     * @return The CustomLocation object.
     */
    public static CustomLocation fromBukkitLocation(Location location) {
        return new CustomLocation(location.getWorld().getName(), location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    /**
     * Returns a CustomLocation object from a string.
     *
     * @param string The string.
     * @return The CustomLocation object.
     */
    public static CustomLocation stringToLocation(String string) {
        String[] split = string.split(", ");

        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Double.parseDouble(split[2]);

        CustomLocation customLocation = new CustomLocation(x, y, z);
        if (split.length == 4) {
            customLocation.setWorld(split[3]);
        } else if (split.length >= 5) {
            customLocation.setYaw(Float.parseFloat(split[3]));
            customLocation.setPitch(Float.parseFloat(split[4]));
            if (split.length >= 6) {
                customLocation.setWorld(split[5]);
            }
        }

        return customLocation;
    }

    /**
     * Returns a string from a CustomLocation object.
     *
     * @param loc The CustomLocation object.
     * @return The string.
     */
    public static String locationToString(CustomLocation loc) {
        StringJoiner joiner = new StringJoiner(", ");
        joiner.add(Double.toString(loc.getX()));
        joiner.add(Double.toString(loc.getY()));
        joiner.add(Double.toString(loc.getZ()));
        if (loc.getYaw() == 0.0f && loc.getPitch() == 0.0f) {
            if (loc.getWorld().equals("world")) {
                return joiner.toString();
            } else {
                joiner.add(loc.getWorld());
                return joiner.toString();
            }
        } else {
            joiner.add(Float.toString(loc.getYaw()));
            joiner.add(Float.toString(loc.getPitch()));
            if (loc.getWorld().equals("world")) {
                return joiner.toString();
            } else {
                joiner.add(loc.getWorld());
                return joiner.toString();
            }
        }
    }

    /**
     * Returns a Bukkit Location object from a CustomLocation object.
     *
     * @return The Bukkit Location object.
     */
    public Location toBukkitLocation() {
        return new Location(this.toBukkitWorld(), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    /**
     * Returns a World object from a CustomLocation object.
     *
     * @return The World object.
     */
    public World toBukkitWorld() {
        if (this.world == null) {
            return Bukkit.getServer().getWorlds().get(0);
        } else {
            return Bukkit.getServer().getWorld(this.world);
        }
    }
}