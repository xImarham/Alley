package dev.revere.alley.profile.division.annotation;

import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
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
public @interface DivisionData {

    /**
     * The name of the division
     *
     * @return the name
     */
    String name();

    /**
     * The description of the division
     *
     * @return the description
     */
    String description();

    /**
     * The icon of the division
     *
     * @return the icon
     */
    Material icon();

    /**
     * The durability of the icon
     *
     * @return the durability
     */
    int durability() default 0;

    /**
     * The tier of the division
     *
     * @return the tier
     */
    EnumDivisionTier tier();

    /**
     * The level of the division
     *
     * @return the level
     */
    EnumDivisionLevel level();

    /**
     * The slot of the division in the GUI
     *
     * @return the slot
     */
    int slot();
}
