package dev.revere.alley.game.match.runnable.other;

import dev.revere.alley.game.match.AbstractMatch;
import dev.revere.alley.game.match.enums.EnumMatchState;
import dev.revere.alley.util.chat.CC;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Emmy
 * @project Alley
 * @since 23/05/2025
 */
public class MatchRespawnRunnable extends BukkitRunnable {
    protected final Player player;
    protected final AbstractMatch match;
    private int count;

    /**
     * Constructor for the MatchRespawnRunnable class.
     *
     * @param player The player to respawn.
     * @param match  The match instance.
     * @param count  The countdown time in seconds.
     */
    public MatchRespawnRunnable(Player player, AbstractMatch match, int count) {
        this.player = player;
        this.match = match;
        this.count = count;
    }

    @Override
    public void run() {
        if (this.count == 0) {
            this.cancel();
            this.match.handleRespawn(this.player);
            return;
        }

        if (this.match.getState() == EnumMatchState.ENDING_MATCH || this.match.getState() == EnumMatchState.ENDING_ROUND) {
            this.cancel();
            return;
        }

        this.player.sendMessage(CC.translate("&a" + this.count + "..."));
        this.count--;
    }
}