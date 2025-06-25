package dev.revere.alley.feature.explosives;

import dev.revere.alley.Alley;
import dev.revere.alley.config.ConfigService;
import dev.revere.alley.feature.explosives.command.ExplosiveCommand;
import dev.revere.alley.feature.explosives.listener.ExplosiveListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
@Getter
@Setter
public class ExplosiveService {
    protected final Alley plugin;

    private double horizontal;
    private double vertical;
    private double range;
    private double speed;

    private int tntFuseTicks;

    /**
     * Constructor for the FireballService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public ExplosiveService(Alley plugin) {
        this.plugin = plugin;

        FileConfiguration settingsConfig = this.plugin.getConfigService().getSettingsConfig();
        if (settingsConfig.getBoolean("explosive.enabled")) {
            this.assignValues(settingsConfig);
            this.registerListener();
            new ExplosiveCommand();
        }
    }

    /**
     * Assigns the values for the explosive knockback settings from the configuration.
     *
     * @param settingsConfig The configuration file containing the settings.
     */
    private void assignValues(FileConfiguration settingsConfig) {
        this.horizontal = settingsConfig.getDouble("explosive.values.horizontal");
        this.vertical = settingsConfig.getDouble("explosive.values.vertical");
        this.range = settingsConfig.getDouble("explosive.values.range");
        this.speed = settingsConfig.getDouble("explosive.values.speed");
        this.tntFuseTicks = settingsConfig.getInt("explosive.values.tnt-fuse-ticks");
    }

    public void save() {
        ConfigService configService = this.plugin.getConfigService();
        FileConfiguration settingsConfig = configService.getSettingsConfig();
        File settingsFile = configService.getConfigFile("settings.yml");

        settingsConfig.set("explosive.values.horizontal", this.horizontal);
        settingsConfig.set("explosive.values.vertical", this.vertical);
        settingsConfig.set("explosive.values.range", this.range);
        settingsConfig.set("explosive.values.speed", this.speed);
        settingsConfig.set("explosive.values.tnt-fuse-ticks", this.tntFuseTicks);

        configService.saveConfig(settingsFile, settingsConfig);
    }

    private void registerListener() {
        this.plugin.getServer().getPluginManager().registerEvents(new ExplosiveListener(this.plugin), this.plugin);
    }
}
