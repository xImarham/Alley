package dev.revere.alley.feature.fireball;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.fireball.command.FireballCommand;
import dev.revere.alley.feature.fireball.listener.FireballListener;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Emmy
 * @project Alley
 * @since 11/06/2025
 */
@Getter
@Setter
public class FireballService {
    protected final Alley plugin;

    private double vertical;
    private double horizontal;
    private double range;

    /**
     * Constructor for the FireballService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public FireballService(Alley plugin) {
        this.plugin = plugin;

        FileConfiguration settingsConfig = this.plugin.getConfigService().getSettingsConfig();
        if (settingsConfig.getBoolean("fireball.enabled")) {
            this.assignValues(settingsConfig);
            this.registerListener();
            new FireballCommand();
        }
    }

    /**
     * Assigns the values for the fireball knockback settings from the configuration.
     *
     * @param settingsConfig The configuration file containing the settings.
     */
    private void assignValues(FileConfiguration settingsConfig) {
        this.vertical = settingsConfig.getDouble("fireball.values.vertical");
        this.horizontal = settingsConfig.getDouble("fireball.values.horizontal");
        this.range = settingsConfig.getDouble("fireball.values.range"); // The range of the explosion effect measured in blocks.
    }

    private void registerListener() {
        this.plugin.getServer().getPluginManager().registerEvents(new FireballListener(this.plugin), this.plugin);
    }
}
