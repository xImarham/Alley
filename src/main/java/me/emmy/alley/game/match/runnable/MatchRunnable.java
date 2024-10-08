package me.emmy.alley.game.match.runnable;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.game.match.AbstractMatch;
import me.emmy.alley.game.match.enums.EnumMatchState;
import me.emmy.alley.util.SoundUtil;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
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
        FileConfiguration config = ConfigHandler.getInstance().getMessagesConfig();

        if (config.getBoolean("match.started.kit-disclaimer.enabled")) {
            if (match.getKit().getDisclaimer() == null) {
                //TODO: get this message from config
                match.sendMessage(CC.translate("&4&lWarning: &cUsing hacks or any form of cheating is strictly prohibited!"));
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
                .forEach(SoundUtil::playBlast)
        ;
    }
}