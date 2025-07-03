package dev.revere.alley.api.assemble;

import dev.revere.alley.api.assemble.interfaces.IAssembleAdapter;
import dev.revere.alley.core.lifecycle.IService;

import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IAssembleService extends IService {
    /**
     * Gets the map of all active scoreboard instances.
     *
     * @return A map where the key is the player's UUID and the value is their AssembleBoard.
     */
    Map<UUID, AssembleBoard> getBoards();

    /**
     * Gets the adapter that is currently providing content (title and lines) to the scoreboard.
     *
     * @return The active IAssembleAdapter instance.
     */
    IAssembleAdapter getAdapter();
}