package me.emmy.alley.tournament.runnable;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.tournament.Tournament;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Emmy
 * Project: alley
 * Date: 30/05/2024 - 21:27
 */

@Getter
@Setter
public class TournamentRunnable extends BukkitRunnable {

    private final Tournament tournament;

    /**
     * Constructor for the TournamentRunnable class.
     *
     * @param tournament The tournament to run.
     */
    public TournamentRunnable(Tournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public void run() {

    }
}
