package dev.revere.alley.api.server;

import dev.revere.alley.core.lifecycle.IService;
import org.bukkit.entity.EntityType;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IServerEnvironment extends IService {
    /**
     * Clears all entities of a specific type from all worlds.
     *
     * @param entityType The type of entity to clear.
     */
    void clearEntities(EntityType entityType);

    /**
     * Clears all entities of any type from all worlds.
     * <p>
     * Caution for people who may use this in their own project:
     * This is destructive and will remove players' mounts, item frames, etc.
     */
    void clearAllEntities();
}