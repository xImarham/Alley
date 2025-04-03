package dev.revere.alley.tool.triple;


/**
 * A generic interface for a container that holds three objects of different types.
 * Provides methods to retrieve each of the three objects.
 *
 * @param <A> the type of the first object
 * @param <B> the type of the second object
 * @param <C> the type of the third object
 *
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
public interface Triple<A, B, C> {
    /**
     * Returns the first object in the triple.
     *
     * @return the first object
     */
    A getA();

    /**
     * Returns the second object in the triple.
     *
     * @return the second object
     */
    B getB();

    /**
     * Returns the third object in the triple.
     *
     * @return the third object
     */
    C getC();
}