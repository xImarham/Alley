package me.emmy.alley.kit.settings;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class KitSetting {

    private final String name;
    private String description;
    private boolean enabled;

    /**
     * Constructor for the KitSetting class.
     *
     * @param name The name of the setting.
     * @param description The description of the setting.
     * @param enabled Whether the setting is enabled.
     */
    public KitSetting(String name, String description, boolean enabled) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
    }
}
