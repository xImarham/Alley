package me.emmy.alley.host.impl.tournament;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.host.impl.tournament.enums.TournamentState;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.util.chat.CC;
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
public class TournamentRepository {
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

    /**
     * Instantiates a new Tournament repository.
     *
     * @param host           the host
     * @param kit            the kit
     * @param requiredPlayers the required players
     * @param maxPlayers     the max players
     */
    public TournamentRepository(Player host, Kit kit, int requiredPlayers, int maxPlayers) {
        this.host = host;
        this.kit = kit;
        this.requiredPlayers = requiredPlayers;
        this.maxPlayers = maxPlayers;
        this.players = new ArrayList<>();
        this.state = TournamentState.WAITING;
    }

    /**
     * Handle teams.
     *
     * @param team the team
     */
    public void handleTeams(TournamentTeam team) {
        team.getPlayers().forEach(player -> playerTeams.put(player, team));
    }

    /**
     * Is player in tournament boolean.
     *
     * @param player the player
     * @return the boolean
     */
    public boolean isPlayerInTournament(Player player) {
        return players.contains(player);
    }

    /**
     * Notify players in the tournament.
     *
     * @param message the message
     */
    public void notifyPlayers(String message) {
        players.forEach(player -> player.sendMessage(CC.translate(message)));
    }
}
