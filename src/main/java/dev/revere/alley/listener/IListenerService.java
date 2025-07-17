package dev.revere.alley.listener;

import dev.revere.alley.Alley;
import dev.revere.alley.plugin.lifecycle.IService;

/**
 * @author Emmy
 * @project alley-practice
 * @since 16/07/2025
 */
public interface IListenerService extends IService {

    /**
     * Registers all listeners for the service.
     * This method should be called during the service initialization phase
     */
    void registerListeners(Alley plugin);
}