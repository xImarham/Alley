package me.emmy.alley.game.ffa.combat;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Infames
 * @date 23/07/2021 @ 20:07
 */
@Getter
public class CombatTagEvent extends PlayerEvent {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player attacker;

    /**
     * Constructor for the CombatTagEvent class.
     *
     * @param player the player
     * @param attacker the attacker
     */
    public CombatTagEvent(Player player, Player attacker) {
        super(player);
        this.attacker = attacker;
    }

    /**
     * Get the handler list.
     *
     * @return the handler list
     * @see HandlerList
     */
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * Get all handlers.
     *
     * @return the handlers
     * @see HandlerList
     */
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}