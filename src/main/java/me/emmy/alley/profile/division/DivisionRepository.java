package me.emmy.alley.profile.division;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.profile.division.enums.EnumDivisionLevel;
import me.emmy.alley.profile.division.enums.EnumDivisionTier;
import me.emmy.alley.profile.division.impl.bronze.BronzeDivision1;
import me.emmy.alley.profile.division.impl.bronze.BronzeDivision2;
import me.emmy.alley.profile.division.impl.bronze.BronzeDivision3;
import me.emmy.alley.profile.division.impl.silver.SilverDivision1;
import me.emmy.alley.profile.division.impl.silver.SilverDivision2;
import me.emmy.alley.profile.division.impl.silver.SilverDivision3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 6/1/2024
 */
@Getter
@Setter
public class DivisionRepository {
    private final List<AbstractDivision> divisions = new ArrayList<>();

    private final List<Class<? extends AbstractDivision>> bronzeDivisions = Arrays.asList(
            BronzeDivision1.class,
            BronzeDivision2.class,
            BronzeDivision3.class
    );

    private final List<Class<? extends AbstractDivision>> silverDivisions = Arrays.asList(
            SilverDivision1.class,
            SilverDivision2.class,
            SilverDivision3.class
    );

    public DivisionRepository() {
        registerDivisions(bronzeDivisions);
        registerDivisions(silverDivisions);
    }

    /**
     * Register a list of division classes to the repository
     *
     * @param classes The classes to register
     */
    private void registerDivisions(List<Class<? extends AbstractDivision>> classes) {
        classes.forEach(this::registerDivision);
    }

    /**
     * Register a division class to the repository
     *
     * @param clazz The class to register
     */
    public void registerDivision(Class<? extends AbstractDivision> clazz) {
        try {
            AbstractDivision instance = clazz.getDeclaredConstructor().newInstance();
            divisions.add(instance);
        } catch (Exception e) {
            System.out.println("Failed to register division class " + clazz.getSimpleName() + "!");
        }
    }

    /**
     * Get a division by its name
     *
     * @param name The name of the division
     * @return The division
     */
    public AbstractDivision getDivision(String name) {
        return divisions.stream()
                .filter(division -> division.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a division by its class
     *
     * @param clazz The class of the division
     * @return The division
     */
    public AbstractDivision getDivision(Class<? extends AbstractDivision> clazz) {
        return divisions.stream()
                .filter(division -> division.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a division by its tier and level
     *
     * @param tier The tier of the division
     * @param level The level of the division
     * @return The division
     */
    public String getDivision(EnumDivisionTier tier, EnumDivisionLevel level) {
        return divisions.stream()
                .filter(division -> division.getTier() == tier && division.getLevel() == level)
                .findFirst()
                .map(AbstractDivision::getName)
                .orElseThrow(() -> new IllegalStateException("Division not found for tier " + tier + " and level " + level));
    }
}
