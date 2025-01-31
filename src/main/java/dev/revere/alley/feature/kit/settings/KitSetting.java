package dev.revere.alley.feature.kit.settings;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.feature.kit.settings.annotation.KitSettingData;

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

    public KitSetting() {
        KitSettingData data = getClass().getAnnotation(KitSettingData.class);
        this.name = data.name();
        this.description = data.description();
        this.enabled = data.enabled();
    }
}