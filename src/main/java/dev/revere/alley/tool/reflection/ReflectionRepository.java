package dev.revere.alley.tool.reflection;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
public class ReflectionRepository {
    protected final Alley plugin;
    private final List<IReflection> reflectionServices;

    /**
     * Constructor for the ReflectionRepository class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ReflectionRepository(Alley plugin) {
        this.plugin = plugin;
        this.reflectionServices = new ArrayList<>();
        this.registerReflectionServices();
    }

    private void registerReflectionServices() {
        Reflections reflections = this.plugin.getPluginConstant().getReflections();
        for (Class<? extends IReflection> clazz : reflections.getSubTypesOf(IReflection.class)) {
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            try {
                IReflection instance;
                try {
                    instance = clazz.getDeclaredConstructor(Alley.class).newInstance(this.plugin);
                } catch (NoSuchMethodException e) {
                    Constructor<? extends IReflection> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    instance = constructor.newInstance();
                }

                this.reflectionServices.add(instance);
            } catch (Exception exception) {
                Logger.logException("Failed to register reflection service: " + clazz.getName(), exception);
            }
        }
    }

    /**
     * Retrieves a reflection service of the specified class type.
     *
     * @param clazz The class type of the service to retrieve.
     * @return The service instance, or null if not found.
     */
    public <T extends IReflection> T getReflectionService(Class<T> clazz) {
        for (IReflection service : this.reflectionServices) {
            if (service.getClass().equals(clazz)) {
                return clazz.cast(service);
            }
        }
        return null;
    }
}