package me.emmy.alley.kit;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.settings.KitSetting;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.queue.Queue;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/05/2024 - 23:30
 */

@Getter
public class KitRepository {
    private final List<Kit> kits = new ArrayList<>();

    public void loadKits() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");

        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            return;
        }

        for (String name : kitsSection.getKeys(false)) {
            String key = "kits." + name;
            ItemStack[] inventory = config.getList(key + ".items").toArray(new ItemStack[0]);
            ItemStack[] armor = config.getList(key + ".armor").toArray(new ItemStack[0]);
            Material icon = Material.matchMaterial(config.getString(key + ".icon"));
            int iconData = config.getInt(key + ".iconData");

            Kit kit = new Kit(
                    name,
                    config.getString(key + ".displayname"),
                    config.getString(key + ".description"),
                    config.getBoolean(key + ".enabled"),
                    config.getInt(key + ".unrankedslot"),
                    config.getInt(key + ".rankedslot"),
                    config.getInt(key + ".editorslot"),
                    inventory,
                    armor,
                    icon,
                    iconData
            );

            loadKitSettings(config, key, kit);
            kits.add(kit);
            addKitToQueue(kit);
        }
    }

    public void saveKits() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");
        for (Kit kit : kits) {
            String key = "kits." + kit.getName();

            config.set(key + ".displayname", kit.getDisplayName());
            config.set(key + ".description", kit.getDescription());
            config.set(key + ".enabled", kit.isEnabled());
            config.set(key + ".unrankedslot", kit.getUnrankedslot());
            config.set(key + ".rankedslot", kit.getRankedslot());
            config.set(key + ".editorslot", kit.getEditorslot());
            config.set(key + ".items", kit.getInventory());
            config.set(key + ".armor", kit.getArmor());
            config.set(key + ".icon", kit.getIcon().name());
            config.set(key + ".icondata", kit.getIconData());
            saveKitSettings(config, key, kit);

        }
        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml"), config);
    }

    /**
     * Method to save the settings of a kit.
     *
     * @param config The configuration file.
     * @param key    The path key.
     * @param kit    The kit.
     */
    private void saveKitSettings(FileConfiguration config, String key, Kit kit) {
        for (KitSetting kitSetting : kit.getKitSettings()) {
            String settingKey = key + ".settings." + kitSetting.getName();
            config.set(settingKey + ".description", kitSetting.getDescription());
            config.set(settingKey + ".enabled", kitSetting.isEnabled());
        }
    }

    /**
     * Method to load the settings of a kit.
     *
     * @param config The configuration file.
     * @param key    The path key.
     * @param kit    The kit.
     */
    private void loadKitSettings(FileConfiguration config, String key, Kit kit) {
        ConfigurationSection settingsSection = config.getConfigurationSection(key + ".settings");
        if (settingsSection == null) {
            return;
        }

        for (String settingName : settingsSection.getKeys(false)) {
            String settingKey = key + ".settings." + settingName;
            String description = config.getString(settingKey + ".description");
            boolean enabled = config.getBoolean(settingKey + ".enabled");

            KitSetting kitSetting = Alley.getInstance().getKitSettingRepository().getSettingByName(settingName);
            if (kitSetting != null) {
                kitSetting.setDescription(description);
                kitSetting.setEnabled(enabled);
                kit.getKitSettings().add(kitSetting);
            }
        }
    }

    public void saveKit(Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");
        String key = "kits." + kit.getName();

        config.set(key + ".displayname", kit.getDisplayName());
        config.set(key + ".description", kit.getDescription());
        config.set(key + ".enabled", kit.isEnabled());
        config.set(key + ".unrankedslot", kit.getUnrankedslot());
        config.set(key + ".rankedslot", kit.getRankedslot());
        config.set(key + ".editorslot", kit.getEditorslot());
        config.set(key + ".items", kit.getInventory());
        config.set(key + ".armor", kit.getArmor());
        config.set(key + ".icon", kit.getIcon().name());
        config.set(key + ".icondata", kit.getIconData());

        saveKitSettings(config, key, kit);

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml"), config);
    }

    private void addKitToQueue(Kit kit) {
        if (!kit.isEnabled()) return;
        new Queue(kit, false);

        if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
            new Queue(kit, true);
        }
    }

    public void deleteKit(Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");
        File file = Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml");

        kits.remove(kit);
        config.set("kits." + kit.getName(), null);

        Alley.getInstance().getConfigHandler().saveConfig(file, config);
    }

    public Kit getKit(String name) {
        return kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
