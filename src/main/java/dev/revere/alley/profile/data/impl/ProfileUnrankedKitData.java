package dev.revere.alley.profile.data.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.division.Division;
import dev.revere.alley.division.DivisionRepository;
import dev.revere.alley.division.tier.DivisionTier;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
@Getter
@Setter
public class ProfileUnrankedKitData {
    private String division;
    private String tier;
    private int wins;
    private int losses;

    public ProfileUnrankedKitData() {
        this.determineDivision();
        this.wins = 0;
        this.losses = 0;
    }

    public void incrementWins() {
        this.wins++;
        this.determineDivision();
    }

    public void incrementLosses() {
        this.losses++;
    }

    public void determineDivision() {
        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        for (Division division : divisionRepository.getDivisions()) {
            for (DivisionTier tier : division.getTiers()) {
                if (this.wins >= tier.getRequiredWins() && (this.division == null || !this.division.equals(division.getName()) || !Objects.equals(this.tier, tier.getName()))) {
                    this.division = division.getName();
                    this.tier = tier.getName();
                }
            }
        }
    }

    /**
     * Gets the division.
     *
     * @return The division.
     */
    public Division getDivision() {
        DivisionRepository divisionRepository = Alley.getInstance().getDivisionRepository();
        return divisionRepository.getDivision(this.division);
    }

    /**
     * Gets the division tier.
     *
     * @return The division tier.
     */
    public DivisionTier getTier() {
        Division division = this.getDivision();
        return division.getTiers().stream()
                .filter(tier -> tier.getName().equals(this.tier))
                .findFirst()
                .orElse(null);
    }
}