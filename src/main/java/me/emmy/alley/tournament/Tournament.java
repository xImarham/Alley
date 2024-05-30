package me.emmy.alley.tournament;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.arena.Arena;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.tournament.enums.EnumTournamentState;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:04
 */

@Getter
@Setter
public class Tournament {
    private List<Player> players;
    private Kit kit;
    private int teamSize;
    private EnumTournamentState enumTournamentState = EnumTournamentState.WAITING;
    private TournamentType tournamentType;
    private boolean running;

    public Tournament(List<Player> players, Kit kit, Arena arena, int teamSize, TournamentType tournamentType) {
        this.players = players;
        this.kit = kit;
        this.teamSize = teamSize;
        this.tournamentType = tournamentType;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    public void announce() {
        List<String> announcement = Arrays.asList(
                "",
                "&d&lTournament",
                " &fA tournament is starting in &d{seconds}&f!",
                " ",
                " &fKit: &d" + kit.getDisplayName(),
                " &fType: &d" + tournamentType.getName(),
                " &fPlayers: &d" + players.size(),
                " &fTeam Size: &d" + teamSize,
                " &f",
                "",
                " &a[CLICK TO ENTER]",
                " "
        );

        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String message : announcement) {
                player.sendMessage(CC.translate(message));
            }
        }
    }
}
