package dev.revere.alley.feature.arena.impl;

import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
import dev.revere.alley.game.match.player.impl.MatchGamePlayerImpl;
import dev.revere.alley.game.match.player.participant.GameParticipant;
import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.arena.Arena;
import dev.revere.alley.feature.arena.enums.EnumArenaType;
import dev.revere.alley.util.location.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @date 20/05/2024 - 19:14
 */
@Setter
@Getter
public class StandAloneArena extends Arena {
    private boolean active = false;

    private Location team1Portal;
    private Location team2Portal;
    private int portalRadius;

    /**
     * Constructor for the StandAloneArena class.
     *
     * @param name The name of the arena.
     * @param minimum The minimum location of the arena.
     * @param maximum The maximum location of the arena.
     */
    public StandAloneArena(String name, Location minimum, Location maximum, Location team1Portal, Location team2Portal) {
        super(name, minimum, maximum);

        if (team1Portal != null) this.team1Portal = team1Portal;
        if (team2Portal != null) this.team2Portal = team2Portal;
        this.portalRadius = Alley.getInstance().getConfigService().getSettingsConfig().getInt("game.portal-radius");
    }

    @Override
    public EnumArenaType getType() {
        return EnumArenaType.STANDALONE;
    }

    @Override
    public void createArena() {
        Alley.getInstance().getArenaRepository().getArenas().add(this);
        saveArena();
    }

    @Override
    public void saveArena() {
        String name = "arenas." + getName();
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");

        config.set(name, null);
        config.set(name + ".type", getType().name());
        config.set(name + ".minimum", LocationUtil.serialize(getMinimum()));
        config.set(name + ".maximum", LocationUtil.serialize(getMaximum()));
        config.set(name + ".center", LocationUtil.serialize(getCenter()));
        config.set(name + ".pos1", LocationUtil.serialize(getPos1()));
        config.set(name + ".pos2", LocationUtil.serialize(getPos2()));
        config.set(name + ".kits", getKits());
        config.set(name + ".enabled", isEnabled());
        config.set(name + ".displayName", getDisplayName());

        if (this.team1Portal != null) config.set(name + ".team1Portal", LocationUtil.serialize(this.team1Portal));
        if (this.team2Portal != null) config.set(name + ".team2Portal", LocationUtil.serialize(this.team2Portal));


        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    @Override
    public void deleteArena() {
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/arenas.yml");
        config.set("arenas." + getName(), null);

        Alley.getInstance().getArenaRepository().getArenas().remove(this);
        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/arenas.yml"), config);
    }

    /**
     * Check if the player is in the enemy portal.
     *
     * @param match The match.
     * @param playerLocation The location of the player.
     * @param playerTeam The team of the player.
     * @return Whether the player is in the enemy portal or not.
     */
    public boolean isEnemyPortal(MatchRoundsRegularImpl match, Location playerLocation, GameParticipant<MatchGamePlayerImpl> playerTeam) {
        Location enemyPortal = playerTeam == match.getParticipantA() ? this.team2Portal : this.team1Portal;
        return playerLocation.distance(enemyPortal) < this.portalRadius;
    }
}