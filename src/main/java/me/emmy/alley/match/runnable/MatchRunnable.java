package me.emmy.alley.match.runnable;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.EnumMatchState;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Remi
 * @project Alley
 * @date 5/25/2024
 */
@Getter
@Setter
public class MatchRunnable extends BukkitRunnable {

    private final AbstractMatch match;
    private int stage;

    public MatchRunnable(AbstractMatch match) {
        this.match = match;
        this.stage = 6;
    }

    @Override
    public void run() {
        stage--;
        switch (match.getMatchState()) {
            case STARTING:
                if (stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), match::handleRoundStart);
                    match.setMatchState(EnumMatchState.RUNNING);
                }
                break;
            case ENDING:
                if (stage == 0) {
                    if (match.canStartRound()) {
                        match.setMatchState(EnumMatchState.STARTING);
                        match.getMatchRunnable().setStage(4);
                    }
                }
                break;
            case ENDED:
                if (stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), match::endMatch);
                }
                break;
        }
    }
}