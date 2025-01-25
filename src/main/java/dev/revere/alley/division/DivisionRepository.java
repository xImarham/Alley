package dev.revere.alley.division;

import dev.revere.alley.Alley;
import dev.revere.alley.division.tier.DivisionTier;
import dev.revere.alley.util.logger.Logger;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 25/01/2025
 */
@Getter
public class DivisionRepository {
    private final List<Division> divisions;
    private final Alley plugin;

    /**
     * Constructor for the DivisionRepository class.
     *
     * @param plugin The Alley plugin instance.
     */
    public DivisionRepository(Alley plugin) {
        this.plugin = plugin;
        this.divisions = new ArrayList<>();
        this.loadDivisions();
    }

    private void loadDivisions() {
        FileConfiguration config = this.plugin.getConfigService().getDivisionsConfig();
        ConfigurationSection divisionsSection = config.getConfigurationSection("divisions");

        if (divisionsSection == null) {
            Logger.logError("No divisions found in the configuration file.");
            return;
        }

        List<Division> loadedDivisions = new ArrayList<>();
        for (String key : divisionsSection.getKeys(false)) {
            if (key == null || key.trim().isEmpty()) {
                Logger.logError("Encountered a null or empty division key. Skipping.");
                continue;
            }

            String path = "divisions." + key;

            String displayName = config.getString(path + ".display-name", key);
            String description = config.getString(path + ".description", "");
            int durability = config.getInt(path + ".durability", 0);
            String iconName = config.getString(path + ".icon");
            Material icon = Material.getMaterial(iconName.toUpperCase());

            List<DivisionTier> tiers = new ArrayList<>();
            ConfigurationSection tierLevels = config.getConfigurationSection(path + ".tier-levels");
            if (tierLevels != null) {
                for (String tierKey : tierLevels.getKeys(false)) {
                    int requiredWins = tierLevels.getInt(tierKey + ".required-wins");
                    tiers.add(new DivisionTier(tierKey, requiredWins));
                }
            }

            Division division = new Division(
                    key,
                    displayName,
                    description,
                    durability,
                    icon,
                    tiers
            );

            loadedDivisions.add(division);
        }

        loadedDivisions.sort(Comparator.comparingInt(d -> d.getTiers().get(0).getRequiredWins()));
        this.divisions.addAll(loadedDivisions);
    }

    /**
     * Save a division to the divisions configuration file.
     *
     * @param division The division to save.
     */
    public void saveDivision(Division division) {
        FileConfiguration config = this.plugin.getConfigService().getDivisionsConfig();
        String path = "divisions." + division.getName();

        config.set(path + ".display-name", division.getDisplayName());
        config.set(path + ".description", division.getDescription());
        config.set(path + ".durability", division.getDurability());
        config.set(path + ".icon", division.getIcon().name());

        for (DivisionTier tier : division.getTiers()) {
            String tierPath = path + ".tier-levels." + tier.getName();
            config.set(tierPath + ".required-wins", tier.getRequiredWins());
        }

        this.plugin.getConfigService().saveConfig(this.plugin.getConfigService().getConfigFile("storage/divisions.yml"), config);
    }

    public void saveDivisions() {
        this.divisions.forEach(this::saveDivision);
    }

    /**
     * Get a division by its name.
     *
     * @param name The name of the division to get.
     * @return The division.
     */
    public Division getDivision(String name) {
        return this.divisions.stream().filter(division -> division.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}