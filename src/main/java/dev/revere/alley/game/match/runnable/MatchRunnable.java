package dev.revere.alley.game.match.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsRegularImpl;
import dev.revere.alley.util.SoundUtil;
import dev.revere.alley.util.chat.CC;
import dev.revere.alley.util.logger.Logger;
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
        this.stage--;
        switch (this.match.getState()) {
            case STARTING:
                if (stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::handleRoundStart);
                    this.match.setState(EnumMatchState.RUNNING);

                    if (this.match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class) && ((MatchRoundsRegularImpl) this.match).getCurrentRound() > 0) {
                        this.match.sendMessage(CC.translate("&aRound Started!"));
                        this.playSoundStarted();
                    } else {
                        this.match.sendMessage(CC.translate("&aMatch has started. Good luck!"));
                        this.playSoundStarted();
                        this.sendDisclaimer();
                    }
                } else {
                    this.match.sendMessage(CC.translate("&a" + this.stage + "..."));
                    this.playSoundStarting();
                }
                break;
            case ENDING_ROUND:
                if (this.stage == 0) {
                    if (this.match.canStartRound()) {
                        this.match.setState(EnumMatchState.STARTING);
                        this.match.getRunnable().setStage(4);
                    }
                }
                break;
            case ENDING_MATCH:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::endMatch);
                }
                break;
        }
    }

    /**
     * Send the disclaimer to the participants.
     */
    private void sendDisclaimer() {
        FileConfiguration config = Alley.getInstance().getConfigService().getMessagesConfig();
        if (config.getBoolean("match.started.kit-disclaimer.enabled")) {
            if (this.match.getKit().getDisclaimer() == null) {
                Alley.getInstance().getConfigService().getMessagesConfig().getStringList("match.started.kit-disclaimer.not-set").forEach(message -> this.match.sendMessage(CC.translate(message)));
                return;
            }

            config.getStringList("match.started.kit-disclaimer.format").forEach(message -> this.match.sendMessage(CC.translate(message)
                    .replace("{kit-disclaimer}", CC.translate(this.match.getKit().getDisclaimer()))
                    .replace("{kit-name}", this.match.getKit().getName())
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