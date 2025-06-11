package dev.revere.alley.base.arena.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.base.arena.AbstractArena;
import dev.revere.alley.base.arena.enums.EnumArenaType;
import dev.revere.alley.game.match.impl.MatchBedImpl;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.tool.serializer.Serializer;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
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
    private final Alley plugin = Alley.getInstance();

    private boolean active = false;

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
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.STANDALONE;
    }

    @Override
    public void createArena() {
        this.plugin.getArenaService().getArenas().add(this);
        this.saveArena();
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
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = this.plugin.getConfigService().getArenasConfig();
        config.set("arenas." + this.getName(), null);

        this.plugin.getArenaService().getArenas().remove(this);
        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/arenas.yml"), config);
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