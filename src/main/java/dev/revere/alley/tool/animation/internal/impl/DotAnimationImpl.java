package dev.revere.alley.tool.animation.internal.impl;

import dev.revere.alley.tool.animation.internal.AbstractAnimation;

import java.util.Arrays;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
public class DotAnimationImpl extends AbstractAnimation {

    public DotAnimationImpl() {
        super(Arrays.asList(".", "..", "..."), 500);
    }
}