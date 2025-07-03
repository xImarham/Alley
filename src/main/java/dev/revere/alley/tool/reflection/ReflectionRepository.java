package dev.revere.alley.tool.reflection;

import dev.revere.alley.Alley;
import dev.revere.alley.api.constant.IPluginConstant;
import dev.revere.alley.core.AlleyContext;
import dev.revere.alley.core.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
@Service(provides = IReflectionRepository.class, priority = 330)
public class ReflectionRepository implements IReflectionRepository {
    private final IPluginConstant pluginConstant;

    private final List<IReflection> reflectionServices = new ArrayList<>();

    /**
     * Constructor for DI.
     */
    public ReflectionRepository(IPluginConstant pluginConstant) {
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.registerReflectionServices();
    }

    private void registerReflectionServices() {
        Reflections reflections = this.pluginConstant.getReflections();
        if (reflections == null) {
            Logger.error("ReflectionRepository cannot initialize: Reflections object is null.");
            return;
        }

        for (Class<? extends IReflection> clazz : reflections.getSubTypesOf(IReflection.class)) {
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            try {
                Constructor<? extends IReflection> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                IReflection instance = constructor.newInstance();
                this.reflectionServices.add(instance);
            } catch (Exception exception) {
                Logger.logException("Failed to register reflection service: " + clazz.getName(), exception);
            }
        }
    }

    @Override
    public <T extends IReflection> T getReflectionService(Class<T> serviceClass) {
        return this.reflectionServices.stream()
                .filter(serviceClass::isInstance)
                .map(serviceClass::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<IReflection> getReflectionServices() {
        return Collections.unmodifiableList(this.reflectionServices);
    }
}