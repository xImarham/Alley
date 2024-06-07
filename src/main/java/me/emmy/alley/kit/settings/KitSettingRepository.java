package me.emmy.alley.kit.settings;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.kit.settings.impl.KitSettingBoxingImpl;
import me.emmy.alley.kit.settings.impl.KitSettingBuildImpl;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.kit.settings.impl.KitSettingSumoImpl;
import me.emmy.alley.utils.chat.CC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class KitSettingRepository {

    private final List<KitSetting> settings = new ArrayList<>();
    private final Map<String, Class<? extends KitSetting>> settingClasses = new HashMap<>();

    public KitSettingRepository() {
        registerSetting(KitSettingBuildImpl.class);
        registerSetting(KitSettingRankedImpl.class);
        registerSetting(KitSettingBoxingImpl.class);
        registerSetting(KitSettingSumoImpl.class);
    }

    /**
     * Method to register a setting class.
     *
     * @param clazz The setting class.
     */
    public void registerSetting(Class<? extends KitSetting> clazz) {
        try {
            KitSetting instance = clazz.getDeclaredConstructor().newInstance();
            settings.add(instance);
            settingClasses.put(instance.getName(), clazz);
        } catch (Exception e) {
            CC.sendError("Failed to register setting class " + clazz.getSimpleName() + "!");
        }
    }

    /**
     * Method to create a new setting instance by its name.
     *
     * @param name The name of the setting.
     * @return A new instance of the setting.
     */
    public KitSetting createSettingByName(String name) {
        Class<? extends KitSetting> clazz = settingClasses.get(name);
        if (clazz != null) {
            try {
                return clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                CC.sendError("Failed to create setting instance for " + name + "!");
            }
        }
        return null;
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

