package me.emmy.alley.tournament;

import lombok.Getter;
import lombok.Setter;
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

    /**
     * Constructor for the Tournament class.
     *
     * @param players        The players in the tournament.
     * @param kit            The kit of the tournament.
     * @param teamSize       The team size of the tournament.
     * @param tournamentType The type of the tournament.
     */
    public Tournament(List<Player> players, Kit kit, int teamSize, TournamentType tournamentType) {
        this.players = players;
        this.kit = kit;
        this.teamSize = teamSize;
        this.tournamentType = tournamentType;
    }

    /**
     * Adds a player to the tournament.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        this.players.add(player);
    }

    /**
     * Removes a player from the tournament.
     *
     * @param player The player to remove.
     */
    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    /**
     * Announces the tournament.
     */
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

    /**
     * Starts the tournament.
     */
    public void start() {
        this.running = true;
        this.enumTournamentState = EnumTournamentState.STARTING;

        setRunning(true);
        setEnumTournamentState(EnumTournamentState.FIGHTING);
        getPlayers().forEach(p -> p.sendMessage("The tournament has started!"));
        getPlayers().forEach(p -> p.getInventory().setContents(getKit().getInventory()));
        getPlayers().forEach(p -> p.getInventory().setArmorContents(getKit().getArmor()));
    }

    /**
     * Ends the tournament.
     */
    public void end() {
        this.running = false;
        this.enumTournamentState = EnumTournamentState.ENDING;

        setRunning(false);
        setEnumTournamentState(EnumTournamentState.WAITING);
        getPlayers().forEach(p -> p.sendMessage("The tournament has ended!"));
    }
}
