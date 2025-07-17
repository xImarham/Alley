package dev.revere.alley.tool.reflection;

import dev.revere.alley.plugin.lifecycle.IService;

import java.util.List;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface IReflectionRepository extends IService {
    /**
     * Gets a list of all discovered reflection service instances.
     * @return An unmodifiable list of reflection services.
     */
    List<IReflection> getReflectionServices();

    /**
     * Retrieves a specific reflection service by its class type.
     *
     * @param serviceClass The class type of the service to retrieve.
     * @param <T> The type of the reflection service.
     * @return The service instance, or null if not found.
     */
    <T extends IReflection> T getReflectionService(Class<T> serviceClass);
}