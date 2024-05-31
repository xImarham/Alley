package me.emmy.alley.profile.cosmetic.killeffects.annotation;

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
    /**
     * The name of the kill effect
     *
     * @return the name of the kill effect
     */
    String name();

    /**
     * The description of the kill effect
     *
     * @return the description of the kill effect
     */
    String description();

    /**
     * The icon of the kill effect
     *
     * @return the icon of the kill effect
     */
    Material icon();

}
