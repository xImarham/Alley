package dev.revere.alley.feature.knockback;

import dev.revere.alley.core.lifecycle.IService;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IKnockbackAdapter extends IService {
    /**
     * Gets the active knockback implementation that was detected during startup.
     *
     * @return The IKnockback implementation for the current server type.
     */
    IKnockback getKnockbackImplementation();
}