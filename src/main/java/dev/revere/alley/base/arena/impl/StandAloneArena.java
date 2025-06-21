package dev.revere.alley.base.arena.impl;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.serializer.Serializer;
import dev.revere.alley.util.FAWEArenaManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
@Setter
@Getter
public class StandAloneArena extends AbstractArena {
    protected final Alley plugin = Alley.getInstance();
    private final FAWEArenaManager faweArenaManager = new FAWEArenaManager();

    private boolean active = false;

    private boolean isTemporaryCopy = false;
    private String originalArenaName;
    private int copyId = -1;

    private Clipboard clipboard;

    private Location team1Portal;
    private Location team2Portal;

    private int portalRadius;
    private int heightLimit;

    /**
     * Constructor for the StandAloneArena class.
     *
     * @param name    The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public StandAloneArena(String name, Location minimum, Location maximum, Location team1Portal, Location team2Portal, int heightLimit) {
        super(name, minimum, maximum);

        if (team1Portal != null) this.team1Portal = team1Portal;
        if (team2Portal != null) this.team2Portal = team2Portal;
        this.portalRadius = this.plugin.getConfigService().getSettingsConfig().getInt("game.portal-radius");
        this.heightLimit = heightLimit;

        if (!isTemporaryCopy) {
            this.clipboard = faweArenaManager.createClipboard(minimum, maximum);
        }
    }

    public StandAloneArena(String originalArenaName, int copyId, Location minimum, Location maximum, Location team1Portal, Location team2Portal, int heightLimit) {
        super(originalArenaName + "-copy-" + copyId, minimum, maximum);
        this.originalArenaName = originalArenaName;
        this.copyId = copyId;
        this.isTemporaryCopy = true;
        this.active = true;

        if (team1Portal != null) this.team1Portal = team1Portal;
        if (team2Portal != null) this.team2Portal = team2Portal;
        this.portalRadius = this.plugin.getConfigService().getSettingsConfig().getInt("game.portal-radius");
        this.heightLimit = heightLimit;
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.STANDALONE;
    }

    @Override
    public void createArena() {
        if (!this.isTemporaryCopy) {
            this.plugin.getArenaService().getArenas().add(this);
            this.saveArena();
            this.clipboard = this.faweArenaManager.createClipboard(this.getMinimum(), this.getMaximum());
        }
    }

    @Override
    public void saveArena() {
        String name = "arenas." + this.getName();
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();

        config.set(name, null);
        config.set(name + ".type", this.getType().name());
        config.set(name + ".minimum", Serializer.serializeLocation(this.getMinimum()));
        config.set(name + ".maximum", Serializer.serializeLocation(this.getMaximum()));
        config.set(name + ".center", Serializer.serializeLocation(this.getCenter()));
        config.set(name + ".pos1", Serializer.serializeLocation(this.getPos1()));
        config.set(name + ".pos2", Serializer.serializeLocation(this.getPos2()));
        config.set(name + ".kits", this.getKits());
        config.set(name + ".enabled", this.isEnabled());
        config.set(name + ".display-name", this.getDisplayName());

        if (this.team1Portal != null)
            config.set(name + ".team-one-portal", Serializer.serializeLocation(this.team1Portal));
        if (this.team2Portal != null)
            config.set(name + ".team-two-portal", Serializer.serializeLocation(this.team2Portal));

        config.set(name + ".height-limit", this.heightLimit);

        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/arenas.yml"), config);

        this.clipboard = this.faweArenaManager.createClipboard(this.getMinimum(), this.getMaximum());
    }

    @Override
    public void deleteArena() {
        if (this.isTemporaryCopy) {
            this.deleteCopiedArena();
            return;
        }
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();
        config.set("arenas." + this.getName(), null);

        this.plugin.getArenaService().getArenas().remove(this);
        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    public void deleteCopiedArena() {
        if (!this.isTemporaryCopy) {
            return;
        }

        faweArenaManager.deleteCopiedArena(this.getMinimum(), this.getMaximum(), () -> {
            this.active = false;
            this.plugin.getArenaService().removeTemporaryArena(this);
        });
    }

    public void verifyArenaExists() {
        if (this.getMinimum() == null || this.getMaximum() == null) {
            Bukkit.broadcastMessage("[Arena] Cannot verify - bounds are null");
            return;
        }

        org.bukkit.World world = this.getMinimum().getWorld();
        if (world == null) {
            Bukkit.broadcastMessage("[Arena] Cannot verify - world is null");
            return;
        }

        Bukkit.broadcastMessage("[Arena] Verifying arena: " + this.getName());
        Bukkit.broadcastMessage("[Arena] Bounds: " + this.getMinimum() + " to " + this.getMaximum());
        Bukkit.broadcastMessage("[Arena] World: " + world.getName());

        // Sample a few blocks to see if they're not air
        int nonAirBlocks = 0;
        int totalSampled = 0;

        Location min = this.getMinimum();
        Location max = this.getMaximum();

        // Sample every 10th block to avoid lag
        for (int x = min.getBlockX(); x <= max.getBlockX(); x += 10) {
            for (int y = min.getBlockY(); y <= max.getBlockY(); y += 10) {
                for (int z = min.getBlockZ(); z <= max.getBlockZ(); z += 10) {
                    org.bukkit.block.Block block = world.getBlockAt(x, y, z);
                    totalSampled++;

                    if (block.getType() != org.bukkit.Material.AIR) {
                        nonAirBlocks++;
                        if (nonAirBlocks <= 5) {
                            Bukkit.broadcastMessage("[Arena] Found block: " + block.getType() + " at " + x + "," + y + "," + z);
                        }
                    }
                }
            }
        }

        Bukkit.broadcastMessage("[Arena] Verification complete: " + nonAirBlocks + "/" + totalSampled + " sampled blocks are non-air");

        if (nonAirBlocks == 0) {
            Bukkit.broadcastMessage("[Arena] WARNING: No non-air blocks found! Arena may not have pasted correctly.");
        } else {
            Bukkit.broadcastMessage("[Arena] Arena appears to exist with " + nonAirBlocks + " non-air blocks sampled.");
        }
    }


    public StandAloneArena createCopy(World targetWorld, Location targetLocation, int copyId) {
        if (isTemporaryCopy) {
            throw new IllegalStateException("Cannot create a copy of a temporary copy arena.");
        }

        Location originalMin = this.getMinimum();
        Location originalMax = this.getMaximum();

        int sizeX = originalMax.getBlockX() - originalMin.getBlockX() + 1;
        int sizeY = originalMax.getBlockY() - originalMin.getBlockY() + 1;
        int sizeZ = originalMax.getBlockZ() - originalMin.getBlockZ() + 1;

        Location newMin = new Location(targetWorld, targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
        Location newMax = new Location(targetWorld, targetLocation.getX() + sizeX - 1, targetLocation.getY() + sizeY - 1, targetLocation.getZ() + sizeZ - 1);

        this.copyBlocksWithFAWE(targetLocation);

        Location newTeam1Portal = null;
        Location newTeam2Portal = null;

        if (this.team1Portal != null) {
            int offsetX = this.team1Portal.getBlockX() - originalMin.getBlockX();
            int offsetY = this.team1Portal.getBlockY() - originalMin.getBlockY();
            int offsetZ = this.team1Portal.getBlockZ() - originalMin.getBlockZ();
            newTeam1Portal = targetLocation.clone().add(offsetX, offsetY, offsetZ);
        }

        if (this.team2Portal != null) {
            int offsetX = this.team2Portal.getBlockX() - originalMin.getBlockX();
            int offsetY = this.team2Portal.getBlockY() - originalMin.getBlockY();
            int offsetZ = this.team2Portal.getBlockZ() - originalMin.getBlockZ();
            newTeam2Portal = targetLocation.clone().add(offsetX, offsetY, offsetZ);
        }

        StandAloneArena copiedArena = new StandAloneArena(
                this.getName(), copyId, newMin, newMax,
                newTeam1Portal, newTeam2Portal, this.heightLimit
        );

        copiedArena.setEnabled(true);
        copiedArena.setDisplayName(this.getDisplayName());
        copiedArena.setKits(this.getKits());

        if (this.getPos1() != null) {
            int offsetX = this.getPos1().getBlockX() - originalMin.getBlockX();
            int offsetY = this.getPos1().getBlockY() - originalMin.getBlockY();
            int offsetZ = this.getPos1().getBlockZ() - originalMin.getBlockZ();
            copiedArena.setPos1(targetLocation.clone().add(offsetX, offsetY, offsetZ));
        }

        if (this.getPos2() != null) {
            int offsetX = this.getPos2().getBlockX() - originalMin.getBlockX();
            int offsetY = this.getPos2().getBlockY() - originalMin.getBlockY();
            int offsetZ = this.getPos2().getBlockZ() - originalMin.getBlockZ();

            Location location = targetLocation.clone().add(offsetX, offsetY, offsetZ);
            location.setYaw(originalMin.getYaw() + 180);

            copiedArena.setPos2(location);
        }

        if (this.getCenter() != null) {
            int offsetX = this.getCenter().getBlockX() - originalMin.getBlockX();
            int offsetY = this.getCenter().getBlockY() - originalMin.getBlockY();
            int offsetZ = this.getCenter().getBlockZ() - originalMin.getBlockZ();
            copiedArena.setCenter(targetLocation.clone().add(offsetX, offsetY, offsetZ));
        }

        Bukkit.broadcastMessage("[Arena] Copy creation completed for: " + copiedArena.getName());
        return copiedArena;
    }

    private void copyBlocksWithFAWE(Location destination) {
        if (this.clipboard == null) {
            Bukkit.broadcastMessage("clipboard is null, so creating a new one.");
            this.clipboard = this.faweArenaManager.createClipboard(this.getMinimum(), this.getMaximum());
        }

        if (this.clipboard != null) {
            Bukkit.broadcastMessage("Pasting clipboard to " + destination);
            faweArenaManager.pasteClipboard(this.clipboard, destination, () -> {
                Bukkit.broadcastMessage("[Arena] FAWE paste operation completed for " + this.getName());
            });
        } else {
            Bukkit.broadcastMessage("Clipboard is null, copying blocks manually.");
            copyBlocks(this.getMinimum(), this.getMaximum(), destination);
        }
    }

    private void copyBlocks(Location sourceMin, Location sourceMax, Location destination) {
        World world = sourceMin.getWorld();
        World destinationWorld = destination.getWorld();

        if (world == null || destinationWorld == null) {
            return;
        }

        for (int x = sourceMin.getBlockX(); x <= sourceMax.getBlockX(); x++) {
            for (int y = sourceMin.getBlockY(); y <= sourceMax.getBlockY(); y++) {
                for (int z = sourceMin.getBlockZ(); z <= sourceMax.getBlockZ(); z++) {
                    Block sourceBlock = world.getBlockAt(x, y, z);

                    int offsetX = x - sourceMin.getBlockX();
                    int offsetY = y - sourceMin.getBlockY();
                    int offsetZ = z - sourceMin.getBlockZ();

                    Block destinationBlock = destinationWorld.getBlockAt(
                            destination.getBlockX() + offsetX,
                            destination.getBlockY() + offsetY,
                            destination.getBlockZ() + offsetZ
                    );

                    destinationBlock.setType(sourceBlock.getType());
                    destinationBlock.setData(sourceBlock.getData());
                }
            }
        }
    }

    /**
     * Check if the player is in the enemy portal.
     *
     * @param match          The match.
     * @param playerLocation The location of the player.
     * @param playerTeam     The team of the player.
     * @return Whether the player is in the enemy portal or not.
     */
    public boolean isEnemyPortal(MatchRoundsImpl match, Location playerLocation, GameParticipant<MatchGamePlayerImpl> playerTeam) {
        Location enemyPortal = playerTeam == match.getParticipantA() ? this.team2Portal : this.team1Portal;
        return playerLocation.distance(enemyPortal) < this.portalRadius;
    }

    /**
     * Check if the block is an enemy bed.
     *
     * @param block              The block to check.
     * @param breakerParticipant The player who is breaking the bed.
     * @return Whether the block is an enemy bed or not.
     */
    public boolean isEnemyBed(Block block, GameParticipant<MatchGamePlayerImpl> breakerParticipant) {
        Location bedLocation = block.getLocation();

        UUID breakerUUID = breakerParticipant.getPlayer().getUuid();
        Profile profile = this.plugin.getProfileService().getProfile(breakerUUID);

        Location spawnA = this.getPos1();
        Location spawnB = this.getPos2();

        MatchBedImpl match = (MatchBedImpl) profile.getMatch();

        boolean isBreakerTeamA = match != null && match.getParticipantA() == breakerParticipant;
        Location ownSpawn = isBreakerTeamA ? spawnA : spawnB;
        Location enemySpawn = isBreakerTeamA ? spawnB : spawnA;

        double distanceToOwn = bedLocation.distanceSquared(ownSpawn);
        double distanceToEnemy = bedLocation.distanceSquared(enemySpawn);

        return distanceToEnemy < distanceToOwn;
    }
}