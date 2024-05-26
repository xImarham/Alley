package me.emmy.alley.kit.settings;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.kit.settings.impl.KitSettingBoxingImpl;
import me.emmy.alley.kit.settings.impl.KitSettingBuildImpl;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class KitSettingRepository {

    private final List<KitSetting> settings = new ArrayList<>();

    public KitSettingRepository() {
        addSetting(new KitSettingBuildImpl());
        addSetting(new KitSettingRankedImpl());
        addSetting(new KitSettingBoxingImpl());
    }

    /**
     * Method to add a setting.
     *
     * @param setting The setting.
     */
    public void addSetting(KitSetting setting) {
        settings.add(setting);
    }

    /**
     * Method to get a setting by its name.
     *
     * @param name The name of the setting.
     * @return The setting.
     */
    public KitSetting getSettingByName(String name) {
        return settings.stream().filter(setting -> setting.getName().equals(name)).findFirst().orElse(null);
    }

    /**
     * Method to get a setting by its class.
     *
     * @param clazz The class of the setting.
     * @return The setting.
     */
    public KitSetting getSettingByClass(Class<? extends KitSetting> clazz) {
        return settings.stream().filter(setting -> setting.getClass().equals(clazz)).findFirst().orElse(null);
    }
}

