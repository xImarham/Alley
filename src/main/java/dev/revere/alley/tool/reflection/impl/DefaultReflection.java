package dev.revere.alley.tool.reflection.impl;

import dev.revere.alley.tool.reflection.IReflection;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public class DefaultReflection implements IReflection {
    /**
     * A single, shared, immutable instance of this reflection utility.
     * This prevents the creation of unnecessary objects.
     */
    public static final IReflection INSTANCE = new DefaultReflection();

    /**
     * Constructor for reflection-based instantiation by `ReflectionRepository`.
     * This constructor must be public for `ReflectionRepository` to successfully
     * create an instance using `getDeclaredConstructor().newInstance()`.
     * The `ReflectionRepository` will manage the lifecycle of this service.
     */
    public DefaultReflection() {}
}