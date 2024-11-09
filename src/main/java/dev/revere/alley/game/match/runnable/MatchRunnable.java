package dev.revere.alley.game.match.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;
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
        switch (match.getState()) {
            case STARTING:
                if (stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), match::handleRoundStart);
                    match.setState(EnumMatchState.RUNNING);
                    match.sendMessage(CC.translate("&aMatch has started. Good luck!"));
                    this.playSoundStarted();
                    this.sendDisclaimer();
                } else {
                    match.sendMessage(CC.translate("&a" + stage + "..."));
                    this.playSoundStarting();
                }
                break;
            case ENDING_ROUND:
                if (stage == 0) {
                    if (match.canStartRound()) {
                        match.setState(EnumMatchState.STARTING);
                        match.getRunnable().setStage(4);
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
     * Send the disclaimer to the participants.
     */
    private void sendDisclaimer() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getMessagesConfig();
        if (config.getBoolean("match.started.kit-disclaimer.enabled")) {
            if (match.getKit().getDisclaimer() == null) {
                Alley.getInstance().getConfigHandler().getMessagesConfig().getStringList("match.started.kit-disclaimer.not-set").forEach(message -> match.sendMessage(CC.translate(message)));
                return;
            }

            config.getStringList("match.started.kit-disclaimer.format").forEach(message -> match.sendMessage(CC.translate(message)
                    .replace("{kit-disclaimer}", CC.translate(match.getKit().getDisclaimer()))
                    .replace("{kit-name}", match.getKit().getName())
            ));
        }
    }

    /**
     * Play the sound during match countdown.
     */
    private void playSoundStarting() {
        this.match.getParticipants().forEach(participant -> SoundUtil.playNeutral(participant.getPlayer().getPlayer()));
    }

    /**
     * Play the sound for the match when it started.
     */
    private void playSoundStarted() {
        this.match.getParticipants().forEach(participant -> SoundUtil.playBlast(participant.getPlayer().getPlayer()));
    }
}