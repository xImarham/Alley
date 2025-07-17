package dev.revere.alley.feature.level;

import dev.revere.alley.config.IConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.feature.level.data.LevelData;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 22/04/2025
 */
@Getter
@Service(provides = ILevelService.class, priority = 160)
public class LevelService implements ILevelService {
    private final IConfigService configService;
    private final List<LevelData> levels = new ArrayList<>();

    /**
     * Constructor for DI.
     */
    public LevelService(IConfigService configService) {
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadLevels();
    }

    @Override
    public List<LevelData> getLevels() {
        return Collections.unmodifiableList(this.levels);
    }

    private void loadLevels() {
        this.levels.clear();
        FileConfiguration config = this.configService.getLevelsConfig();
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

    @Override
    public void createLevel(String name, int minElo, int maxElo) {
        LevelData level = new LevelData(name, "&6" + name, Material.DIAMOND_SWORD, 0, minElo, maxElo);
        this.levels.add(level);
        this.saveLevel(level);
    }

    @Override
    public void deleteLevel(LevelData level) {
        if (!this.levels.remove(level)) return;

        FileConfiguration config = this.configService.getLevelsConfig();
        File file = this.configService.getConfigFile("storage/levels.yml");

        config.set("levels." + level.getName(), null);
        this.configService.saveConfig(file, config);
    }

    @Override
    public void saveLevel(LevelData level) {
        FileConfiguration config = this.configService.getLevelsConfig();
        File file = this.configService.getConfigFile("storage/levels.yml");
        String path = "levels." + level.getName();

        config.set(path + ".display-name", level.getDisplayName());
        config.set(path + ".material", level.getMaterial().name());
        config.set(path + ".durability", level.getDurability());
        config.set(path + ".min-elo", level.getMinElo());
        config.set(path + ".max-elo", level.getMaxElo());

        this.configService.saveConfig(file, config);
    }

    @Override
    public LevelData getLevel(int elo) {
        return this.levels.stream()
                .filter(tier -> elo >= tier.getMinElo() && elo <= tier.getMaxElo())
                .findFirst()
                .orElse(null);
    }

    @Override
    public LevelData getLevel(String name) {
        return this.levels.stream()
                .filter(tier -> tier.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}