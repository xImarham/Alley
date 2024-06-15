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
 * Created by Emmy
 * Project: Alley
 * Date: 15/06/2024 - 16:53
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

    public TournamentTeam(Player leader, Party party) {
        this.leader = leader;
        this.party = party;
        this.players = new ArrayList<>();
    }

    public Stream<Player> getPlayers() {
        return this.players.stream();
    }

    public void removePlayer(Player player) {
        this.alivePlayers.remove(player);
        this.deadPlayers.add(player);
    }

    public void respawnPlayer(Player player) {

        //for an admin command (/respawn <player>)

        this.players.add(player);
        this.alivePlayers.add(player);
    }

    public Stream<Player> alivePlayers() {
        return this.alivePlayers.stream();
    }


}
