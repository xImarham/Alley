package me.emmy.alley.config;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Emmy
 * Project: Alley
 * Discord: dsc.gg/emmiesa
 */

@Getter
public class ConfigHandler {

    private final Alley plugin = Alley.getInstance();

    /**
     * Example Usage: Alley.getInstance().getConfigHandler().getMessagesConfig().getString("example");
     */
    private final FileConfiguration messagesConfig = getConfigByName("messages.yml");
    private final FileConfiguration settingsConfig = getConfigByName("settings.yml");
    private final FileConfiguration databaseConfig = getConfigByName("database/database.yml");
    private final FileConfiguration ffaConfig = getConfigByName("storage/ffa.yml");
    private final FileConfiguration kitsConfig = getConfigByName("storage/kits.yml");
    private final FileConfiguration arenasConfig = getConfigByName("storage/arenas.yml");
    private final FileConfiguration scoreboardConfig = getConfigByName("providers/scoreboard.yml");

    private final Map<String, File> configFiles = new HashMap<>();
    private final Map<String, FileConfiguration> fileConfigurations = new HashMap<>();

    private final String[] configFileNames = {
            "settings.yml", "messages.yml", "database/database.yml", "storage/ffa.yml", "storage/kits.yml", "storage/arenas.yml", "providers/scoreboard.yml"
    };

    public ConfigHandler() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    private void loadConfig(String fileName) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        configFiles.put(fileName, configFile);
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(fileName, false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        fileConfigurations.put(fileName, config);
    }

    public void reloadConfigs() {
        for (String fileName : configFileNames) {
            loadConfig(fileName);
        }
    }

    public void saveConfigs() {
        for (Map.Entry<String, FileConfiguration> entry : fileConfigurations.entrySet()) {
            String fileName = entry.getKey();
            FileConfiguration config = entry.getValue();
            File configFile = configFiles.get(fileName);
            saveConfig(configFile, config);
        }
    }

    public void saveConfig(File configFile, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(configFile);
            fileConfiguration.load(configFile);
        } catch (Exception e) {
            CC.sendError("Error occurred while saving config: " + configFile.getName());
        }
    }

    public FileConfiguration getConfigByName(String fileName) {
        return fileConfigurations.get(fileName);
    }

    public File getConfigFileByName(String fileName) {
        return configFiles.get(fileName);
    }
}