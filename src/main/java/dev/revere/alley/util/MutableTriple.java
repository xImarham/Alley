package dev.revere.alley.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project Alley
 * @date 5/27/2024
 */
@Getter
@Setter
public class MutableTriple<A, B,C> {
    private A a;
    private B b;
    private C c;

    /**
     * Constructor for the MutableTriple class.
     *
     * @param a the first object
     * @param b the second object
     * @param c the third object
     */
    public MutableTriple(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}