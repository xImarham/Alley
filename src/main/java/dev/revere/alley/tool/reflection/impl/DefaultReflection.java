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
     * Private constructor to ensure this class is only instantiated
     * internally, promoting the use of the singleton INSTANCE field.
     */
    private DefaultReflection() {}
}