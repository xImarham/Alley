package dev.revere.alley.game.bot.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.game.bot.BotFight;
import dev.revere.alley.game.bot.enums.EnumBotFightState;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 16/04/2025
 */
@Getter
@Setter
public class BotFightRunnable extends BukkitRunnable {
    private final BotFight botFight;
    private int stage;

    /**
     * Constructor for the BotFightRunnable class.
     *
     * @param botFight The BotFight instance associated with this runnable.
     */
    public BotFightRunnable(BotFight botFight) {
        this.botFight = botFight;
        this.stage = 5;
    }

    @Override
    public void run() {
        switch (this.botFight.getState()) {
            case STARTING:
                if (this.stage == 0) {
                    this.botFight.setupBot();
                    this.botFight.setState(EnumBotFightState.FIGHTING);
                    this.botFight.sendMessage(CC.translate("&aMatch has started. Good luck!"));
                    BotFightActionRunnable actionRunnable = new BotFightActionRunnable(this.botFight);
                    actionRunnable.runTaskTimer(Alley.getInstance(), 0L, 1L);
                } else {
                    this.botFight.sendMessage(CC.translate("&a" + this.stage + "..."));
                }
                break;
            case ENDING:
                if (this.stage == 0) {
                    Alley.getInstance().getServer().getScheduler().runTask(Alley.getInstance(), this.botFight::endFight);
                }
                break;
        }

        this.stage--;
    }
}
