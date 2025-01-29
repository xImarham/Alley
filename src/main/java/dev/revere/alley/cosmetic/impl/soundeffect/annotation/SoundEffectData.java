package dev.revere.alley.cosmetic.impl.soundeffect.annotation;

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
public @interface SoundEffectData {
    String name();

    String description();

    String permission() default "";

    Material icon();

    int slot();

    int price() default 500;
}
