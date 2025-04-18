package dev.revere.alley.game.bot.runnable;

import dev.revere.alley.Alley;
import dev.revere.alley.game.bot.Bot;
import dev.revere.alley.game.bot.BotFight;
import dev.revere.alley.game.bot.enums.EnumBotFightState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 18/04/2025
 */
public class BotFightActionRunnable extends BukkitRunnable {
    private final BotFight botFight;

    /**
     * Constructor for the BotFightActionRunnable class.
     *
     * @param botFight The BotFight instance associated with this runnable.
     */
    public BotFightActionRunnable(BotFight botFight) {
        this.botFight = botFight;
    }

    @Override
    public void run() {
        Bot bot = botFight.getBot();
        Player player = botFight.getPlayer();

        if (this.botFight.getState() != EnumBotFightState.FIGHTING) {
            this.cancel();
            return;
        }

        if (bot == null || player == null || player.isDead() || !bot.getNpc().isSpawned()) {
            this.cancel();
            return;
        }

        if (bot.getNpc().getEntity() instanceof LivingEntity) {
            LivingEntity botEntity = (LivingEntity) bot.getNpc().getEntity();
            double distance = botEntity.getLocation().distanceSquared(player.getLocation());

            Alley.getInstance().getBotMechanics().handleBotAim(bot, player);

            if (distance < 400) { // 20 blocks
                Alley.getInstance().getBotMechanics().handleAttackRange(distance, player, bot);
            }
        }
    }
}