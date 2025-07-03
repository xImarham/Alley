package dev.revere.alley.server;

import dev.revere.alley.core.lifecycle.IService;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface ICoreAdapter extends IService {

    /**
     * Gets the active core implementation that was detected during startup.
     *
     * @return The ICore implementation for the currently enabled core plugin.
     */
    ICore getCore();
}