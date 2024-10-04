package me.emmy.alley.host.impl.tournament;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.party.Party;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author Emmy
 * @project Alley
 * @date 15/06/2024 - 16:53
 */
@Getter
@Setter
public class TournamentTeam {
    private final Map<Player, String> playerNames = new HashMap<>();
    private final List<Player> alivePlayers = new ArrayList<>();
    private final List<Player> deadPlayers = new ArrayList<>();
    private final List<Player> spectators = new ArrayList<>();
    private final List<Player> players;
    private Player leader;
    private Party party;

    /**
     * Instantiates a new Tournament team.
     *
     * @param leader the leader
     * @param party  the party
     */
    public TournamentTeam(Player leader, Party party) {
        this.leader = leader;
        this.party = party;
        this.players = new ArrayList<>();
    }

    /**
     * Gets players.
     *
     * @return the players
     */
    public Stream<Player> getPlayers() {
        return this.players.stream();
    }

    /**
     * Remove a player from the tournament team.
     *
     * @param player the player
     */
    public void removePlayer(Player player) {
        this.alivePlayers.remove(player);
        this.deadPlayers.add(player);
    }

    /**
     * Get alive players.
     *
     * @return the stream
     */
    public Stream<Player> alivePlayers() {
        return this.alivePlayers.stream();
    }
}