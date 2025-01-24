package dev.revere.alley.profile.division;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.profile.division.enums.EnumDivisionLevel;
import dev.revere.alley.profile.division.enums.EnumDivisionTier;
import dev.revere.alley.profile.division.impl.bronze.BronzeDivision1;
import dev.revere.alley.profile.division.impl.bronze.BronzeDivision2;
import dev.revere.alley.profile.division.impl.bronze.BronzeDivision3;
import dev.revere.alley.profile.division.impl.diamond.DiamondDivision1;
import dev.revere.alley.profile.division.impl.diamond.DiamondDivision2;
import dev.revere.alley.profile.division.impl.diamond.DiamondDivision3;
import dev.revere.alley.profile.division.impl.gold.GoldDivision1;
import dev.revere.alley.profile.division.impl.gold.GoldDivision2;
import dev.revere.alley.profile.division.impl.gold.GoldDivision3;
import dev.revere.alley.profile.division.impl.grandmaster.GrandmasterDivision1;
import dev.revere.alley.profile.division.impl.grandmaster.GrandmasterDivision2;
import dev.revere.alley.profile.division.impl.grandmaster.GrandmasterDivision3;
import dev.revere.alley.profile.division.impl.master.MasterDivision1;
import dev.revere.alley.profile.division.impl.master.MasterDivision2;
import dev.revere.alley.profile.division.impl.master.MasterDivision3;
import dev.revere.alley.profile.division.impl.platinum.PlatinumDivision1;
import dev.revere.alley.profile.division.impl.platinum.PlatinumDivision2;
import dev.revere.alley.profile.division.impl.platinum.PlatinumDivision3;
import dev.revere.alley.profile.division.impl.silver.SilverDivision1;
import dev.revere.alley.profile.division.impl.silver.SilverDivision2;
import dev.revere.alley.profile.division.impl.silver.SilverDivision3;

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

    private final List<Class<? extends AbstractDivision>> goldDivisions = Arrays.asList(
            GoldDivision1.class,
            GoldDivision2.class,
            GoldDivision3.class
    );

    private final List<Class<? extends AbstractDivision>> platinumDivisions = Arrays.asList(
            PlatinumDivision1.class,
            PlatinumDivision2.class,
            PlatinumDivision3.class
    );

    private final List<Class<? extends AbstractDivision>> diamondDivisions = Arrays.asList(
            DiamondDivision1.class,
            DiamondDivision2.class,
            DiamondDivision3.class
    );

    private final List<Class<? extends AbstractDivision>> masterDivisions = Arrays.asList(
            MasterDivision1.class,
            MasterDivision2.class,
            MasterDivision3.class
    );

    private final List<Class<? extends AbstractDivision>> grandmasterDivisions = Arrays.asList(
            GrandmasterDivision1.class,
            GrandmasterDivision2.class,
            GrandmasterDivision3.class
    );

    public DivisionRepository() {
        this.registerDivisions(this.bronzeDivisions);
        this.registerDivisions(this.silverDivisions);
        this.registerDivisions(this.goldDivisions);
        this.registerDivisions(this.platinumDivisions);
        this.registerDivisions(this.diamondDivisions);
        this.registerDivisions(this.masterDivisions);
        this.registerDivisions(this.grandmasterDivisions);
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
            this.divisions.add(instance);
        } catch (Exception exception) {
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
        return this.divisions.stream()
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
        return this.divisions.stream()
                .filter(division -> division.getClass().equals(clazz))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get a division by its tier and level
     *
     * @param tier  The tier of the division
     * @param level The level of the division
     * @return The division
     */
    public String getDivision(EnumDivisionTier tier, EnumDivisionLevel level) {
        return this.divisions.stream()
                .filter(division -> division.getTier() == tier && division.getLevel() == level)
                .findFirst()
                .map(AbstractDivision::getName)
                .orElseThrow(() -> new IllegalStateException("Division not found for tier " + tier + " and level " + level));
    }
}