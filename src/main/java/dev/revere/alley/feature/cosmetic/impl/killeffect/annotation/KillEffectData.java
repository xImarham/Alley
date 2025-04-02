package dev.revere.alley.feature.cosmetic.impl.killeffect.annotation;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KillEffectData {
    String name();

    String description();

    String permission() default "";

    Material icon();

    int slot();

    int price() default 500;
}
