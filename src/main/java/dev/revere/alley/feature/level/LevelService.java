package dev.revere.alley.feature.level;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.level.data.LevelData;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
public class LevelService {
    protected final Alley plugin;
    private final List<LevelData> levels;

    /**
     * Constructor for the LevelService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public LevelService(Alley plugin) {
        this.plugin = plugin;
        this.levels = new ArrayList<>();
        this.loadLevels();
    }

    private void loadLevels() {
        FileConfiguration config = this.plugin.getConfigService().getLevelsConfig();

        ConfigurationSection levelsSection = config.getConfigurationSection("levels");
        if (levelsSection == null) {
            return;
        }

        for (String key : levelsSection.getKeys(false)) {
            String path = "levels." + key;

            String displayName = config.getString(path + ".display-name");

            Material material = Material.matchMaterial(config.getString(path + ".material"));
            int durability = config.getInt(path + ".durability");

            int minElo = config.getInt(path + ".min-elo");
            int maxElo = config.getInt(path + ".max-elo");

            LevelData level = new LevelData(key, displayName, material, durability, minElo, maxElo);
            this.levels.add(level);
        }
    }

    /**
     * Creates a new level tier.
     *
     * @param name   Name of the level.
     * @param minElo Minimum Elo rating for this level.
     * @param maxElo Maximum Elo rating for this level.
     */
    public void createLevel(String name, int minElo, int maxElo) {
        LevelData level = new LevelData(name, "&b" + name, Material.DIAMOND_SWORD, 0, minElo, maxElo);
        this.levels.add(level);
        this.saveLevel(level);
    }

    /**
     * Deletes an existing level record.
     *
     * @param level LevelData object containing the level data to be deleted.
     */
    public void deleteLevel(LevelData level) {
        this.levels.remove(level);
        FileConfiguration config = this.plugin.getConfigService().getLevelsConfig();
        File file = this.plugin.getConfigService().getConfigFile("storage/levels.yml");

        String path = "levels." + level.getName();
        config.set(path, null);

        this.plugin.getConfigService().saveConfig(file, config);
    }

    /**
     * Saves a level tier to the configuration file.
     *
     * @param level LevelData object containing the level data.
     */
    public void saveLevel(LevelData level) {
        FileConfiguration config = this.plugin.getConfigService().getLevelsConfig();
        File file = this.plugin.getConfigService().getConfigFile("storage/levels.yml");

        String path = "levels." + level.getName();

        config.set(path + ".display-name", level.getDisplayName());
        config.set(path + ".material", level.getMaterial().name());
        config.set(path + ".durability", level.getDurability());
        config.set(path + ".min-elo", level.getMinElo());
        config.set(path + ".max-elo", level.getMaxElo());

        this.plugin.getConfigService().saveConfig(file, config);
    }

    /**
     * Gets the level that matches a given Elo.
     *
     * @param elo Elo rating.
     * @return Matching level tier or null.
     */
    public LevelData getLevel(int elo) {
        return this.levels.stream().filter(tier -> elo >= tier.getMinElo() && elo <= tier.getMaxElo()).findFirst().orElse(null);
    }

    /**
     * Gets the level that matches a given name.
     *
     * @param name Name of the level.
     * @return Matching level tier or null.
     */
    public LevelData getLevel(String name) {
        return this.levels.stream().filter(tier -> tier.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}