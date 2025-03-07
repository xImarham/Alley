package dev.revere.alley.api.assemble.events;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.api.assemble.AssembleBoard;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
@Setter
public class AssembleBoardCreatedEvent extends Event {

    @Getter
    public static HandlerList handlerList = new HandlerList();

    private boolean cancelled;
    private final AssembleBoard board;

    /**
     * Assemble Board Created Event.
     *
     * @param board of player.
     */
    public AssembleBoardCreatedEvent(AssembleBoard board) {
        this.cancelled = false;
        this.board = board;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}