package me.emmy.alley.match.runnable;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.match.AbstractMatch;
import me.emmy.alley.match.enums.EnumMatchState;
import me.emmy.alley.util.SoundUtil;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
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

    /**
     * Constructor for the MatchRunnable class.
     *
     * @param match The match.
     */
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
                    match.sendMessage(CC.translate("&aMatch has started. Good luck!"));
                    playSoundStarted();
                } else {
                    match.sendMessage(CC.translate("&a" + stage + "..."));
                    playSoundStarting();
                }
                break;
            case ENDING_ROUND:
                if (stage == 0) {
                    if (match.canStartRound()) {
                        match.setMatchState(EnumMatchState.STARTING);
                        match.getMatchRunnable().setStage(4);
                    }
                }
                break;
            case ENDING_MATCH:
                if (stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), match::endMatch);
                }
                break;
        }
    }

    /**
     * Play the sound during match countdown.
     */
    private void playSoundStarting() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> match.getParticipants().stream().anyMatch(participant -> participant.containsPlayer(player.getUniqueId())))
                .forEach(SoundUtil::playNeutral)
        ;
    }

    /**
     * Play the sound for the match when it started.
     */
    private void playSoundStarted() {
        Bukkit.getOnlinePlayers().stream()
                .filter(player -> match.getParticipants().stream().anyMatch(participant -> participant.containsPlayer(player.getUniqueId())))
                .forEach(SoundUtil::playSuccess)
        ;
    }
}