package me.emmy.alley.host.impl.tournament;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.host.impl.tournament.enums.TournamentState;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Emmy
 * @project Alley
 * @date 14/06/2024 - 22:26
 */
@Getter
@Setter
public class Tournament {

    private final Map<Player, TournamentTeam> playerTeams = new HashMap<>();
    private List<Player> players;
    private TournamentState state;
    private int requiredPlayers;
    private int maxPlayers;
    private int countdown = 60;
    private int round = 1;
    private Player winner;
    private Player host;
    private Kit kit;

    private boolean running = false;

    public Tournament(Player host, Kit kit, int requiredPlayers, int maxPlayers) {
        this.host = host;
        this.kit = kit;
        this.requiredPlayers = requiredPlayers;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.state = TournamentState.WAITING;
    }

    public void join(Player player) {

    }

    public void handleTeams(TournamentTeam team) {
        team.getPlayers().forEach(player -> playerTeams.put(player, team));
    }

    public void setupPlayer(Player player) {

    }

    public void handleDeath(Player player, Player killer) {

    }

    public void handleRoundEnd(Player player) {

    }

    public void handleRoundStart() {

    }

    public void leave(Player player) {

    }

    public boolean isPlayerInTournament(Player player) {
        return players.contains(player);
    }

    public void notifyPlayers(String message) {
        players.forEach(player -> player.sendMessage(CC.translate(message)));
    }
}
