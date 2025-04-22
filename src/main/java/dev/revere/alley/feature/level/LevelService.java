package dev.revere.alley.feature.level;

import dev.revere.alley.Alley;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

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
    private final List<Level> levels;

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

            Material material = Material.matchMaterial(config.getString(path));
            int durability = config.getInt(path + ".durability");

            int minElo = config.getInt(path + ".min-elo");
            int maxElo = config.getInt(path + ".max-elo");

            Level level = new Level(key, displayName, material, durability, minElo, maxElo);
            this.levels.add(level);
        }
    }

    /**
     * Gets the level that matches a given Elo.
     *
     * @param elo Elo rating.
     * @return Matching level tier or null.
     */
    public Level getLevel(int elo) {
        return this.levels.stream().filter(tier -> elo >= tier.getMinElo() && elo <= tier.getMaxElo()).findFirst().orElse(null);
    }

    /**
     * Gets the level that matches a given name.
     *
     * @param name Name of the level.
     * @return Matching level tier or null.
     */
    public Level getLevel(String name) {
        return this.levels.stream().filter(tier -> tier.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}