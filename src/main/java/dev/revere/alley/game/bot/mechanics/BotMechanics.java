package dev.revere.alley.game.bot.mechanics;

import dev.revere.alley.game.bot.Bot;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Emmy
 * @project Alley
 * @since 18/04/2025
 */
public class BotMechanics {
    /**
     * Handles the bot's aim towards the player.
     *
     * @param bot    The bot instance.
     * @param player The player instance.
     */
    public void handleBotAim(Bot bot, Player player) {
        double aimRandomness = bot.getPreset().getAimRandomness();
        bot.getNpc().faceLocation(player.getLocation().add(new Vector(
            (Math.random() - 0.5) * aimRandomness,
            (Math.random() - 0.5) * aimRandomness,
            (Math.random() - 0.5) * aimRandomness
        )));
    }

    /**
     * Handles the attack range logic for the bot.
     *
     * @param distance The distance between the bot and the player.
     * @param player   The player instance.
     * @param bot      The bot instance.
     */
    public void handleAttackRange(double distance, Player player, Bot bot) {
        double attackRange = bot.getPreset().getReach();
        if (distance <= attackRange * attackRange) {
            player.damage(this.getDamageBasedOnItem(bot));
        } else {
            bot.getNpc().getNavigator().setTarget(player.getLocation());
        }

        bot.getNpc().getNavigator().setPaused(false);
    }

    /**
     * Calculates the damage based on the item equipped by the bot.
     *
     * @param bot The bot whose item is being checked.
     * @return The damage value.
     */
    private double getDamageBasedOnItem(Bot bot) {
        return 2.0;

        //TODO: Implement logic to check the item equipped by the bot and return the corresponding damage value.
    }
}