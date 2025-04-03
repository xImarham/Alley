package dev.revere.alley.tool.animation.type.internal.impl;

import dev.revere.alley.tool.animation.type.internal.AbstractAnimation;

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