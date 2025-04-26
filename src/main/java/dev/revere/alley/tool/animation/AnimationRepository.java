package dev.revere.alley.tool.animation;

import dev.revere.alley.Alley;
import dev.revere.alley.tool.animation.enums.EnumAnimationType;
import dev.revere.alley.tool.animation.type.config.TextAnimation;
import dev.revere.alley.tool.animation.type.internal.AbstractAnimation;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles the registration and retrieval of both internal and config-based animations.
 * Uses reflection to automatically register animations dynamically.
 *
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
public class AnimationRepository {
    protected final Alley plugin;

    private final Set<AbstractAnimation> internalAnimations;
    private final Set<TextAnimation> configAnimations;

    private final Reflections reflections;

    /**
     * Constructor for the AnimationRepository class.
     *
     * @param plugin The Alley plugin instance.
     */
    public AnimationRepository(Alley plugin) {
        this.plugin = plugin;

        this.internalAnimations = new HashSet<>();
        this.configAnimations = new HashSet<>();

        this.reflections = plugin.getPluginConstant().getReflections();

        this.registerAnimations(AbstractAnimation.class, this.internalAnimations);
        this.registerAnimations(TextAnimation.class, this.configAnimations);
    }

    /**
     * Scans and registers all non-abstract animation classes of the given type.
     *
     * @param <T>        The animation type.
     * @param superClass The base class of animations to register.
     * @param targetSet  The collection where instances should be stored.
     */
    private <T> void registerAnimations(Class<T> superClass, Set<T> targetSet) {
        Set<Class<? extends T>> classes = this.reflections.getSubTypesOf(superClass)
                                              .stream()
                                              .filter(clazz -> !Modifier.isAbstract(clazz.getModifiers()))
                                              .collect(Collectors.toSet());

        for (Class<? extends T> clazz : classes) {
            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                targetSet.add(instance);
            } catch (Exception e) {
                Logger.logException(String.format("Failed to instantiate animation: %s", clazz.getName()), e);
            }
        }
    }

    /**
     * Retrieves an animation instance of the specified class from either internal or config animators.
     *
     * @param clazz The class of the animator to retrieve.
     * @param type  The type of animator to search for (INTERNAL or CONFIG).
     * @param <T>   The type of the animator.
     * @return The requested animator.
     * @throws IllegalArgumentException if the animator is not found.
     */
    public <T> T getAnimation(Class<T> clazz, EnumAnimationType type) {
        Set<?> targetSet;

        switch (type) {
            case INTERNAL:
                targetSet = this.internalAnimations;
                break;
            case CONFIG:
                targetSet = this.configAnimations;
                break;
            default:
                throw new IllegalArgumentException("Invalid animation type: " + type);
        }

        return targetSet.stream()
                   .filter(clazz::isInstance)
                   .map(clazz::cast)
                   .findFirst()
                   .orElseThrow(() -> new IllegalArgumentException("No animator found for class: " + clazz.getName()));
    }
}