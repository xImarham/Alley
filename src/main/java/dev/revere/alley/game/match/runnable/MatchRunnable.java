package dev.revere.alley.game.match.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingBattleRushImpl;
import dev.revere.alley.feature.kit.settings.impl.KitSettingStickFightImpl;
import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.game.match.impl.MatchRoundsImpl;
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
        this.stage--;

        if (this.hasToEnd()) return;

        switch (this.match.getState()) {
            case STARTING:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.match::handleRoundStart);
                    this.match.setState(EnumMatchState.RUNNING);

                    if (this.match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class) || this.match.getKit().isSettingEnabled(KitSettingStickFightImpl.class) && ((MatchRoundsImpl) this.match).getCurrentRound() > 0) {
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
                if (this.match.canStartRound()) {
                    this.match.setState(EnumMatchState.STARTING);
                    this.match.getRunnable().setStage(4);
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
     * End the match if the time is up based on different kit settings.
     *
     * @return If the time limit has been reached.
     */
    private boolean hasToEnd() {
        long elapsedTime = System.currentTimeMillis() - match.getStartTime();
        if (this.match.getKit().isSettingEnabled(KitSettingBattleRushImpl.class)) {
            return checkTime(elapsedTime, 900_000); // 15 minutes
        } else {
            return this.checkTime(elapsedTime, 1800_000); // 30 minutes (default)
        }
    }

    /**
     * Check if the time is up in the match.
     *
     * @param elapsedTime The elapsed time.
     * @param timeLimit   The time limit.
     * @return If the match ended.
     */
    private boolean checkTime(long elapsedTime, long timeLimit) {
        if (this.match.getState() == EnumMatchState.RUNNING && elapsedTime >= timeLimit) {
            this.match.sendMessage(CC.translate("&cMatch has ended due to time limit!"));
            this.match.setState(EnumMatchState.ENDING_MATCH);
            this.stage = 4;
            return true;
        }
        return false;
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

    private void playSoundStarting() {
        this.match.getParticipants().forEach(participant -> SoundUtil.playNeutral(participant.getPlayer().getPlayer()));
    }

    private void playSoundStarted() {
        this.match.getParticipants().forEach(participant -> SoundUtil.playBlast(participant.getPlayer().getPlayer()));
    }
}