package me.emmy.alley.kit;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.config.ConfigHandler;
import me.emmy.alley.kit.settings.KitSetting;
import me.emmy.alley.kit.settings.impl.KitSettingRankedImpl;
import me.emmy.alley.queue.Queue;
import me.emmy.alley.util.chat.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 19/05/2024 - 23:30
 */
@Getter
public class KitRepository {
    private final List<Kit> kits = new ArrayList<>();

    public KitRepository() {
        loadKits();
    }

    /**
     * Method to load all kits from the kits.yml file.
     */
    public void loadKits() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/kits.yml");
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            return;
        }

        for (String name : kitsSection.getKeys(false)) {
            String key = "kits." + name;
            ItemStack[] inventory = config.getList(key + ".items").toArray(new ItemStack[0]);
            ItemStack[] armor = config.getList(key + ".armor").toArray(new ItemStack[0]);
            Material icon = Material.matchMaterial(config.getString(key + ".icon"));
            int iconData = config.getInt(key + ".icondata");
            String disclaimer = config.getString(key + ".disclaimer");

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
                    iconData,
                    disclaimer
            );

            loadKitSettings(config, key, kit);
            addMissingKitSettings(kit, config, key);
            kits.add(kit);
            addKitToQueue(kit);
        }
    }

    /**
     * Method to save all kits to the kits.yml file.
     */
    public void saveKits() {
        for (Kit kit : kits) {
            FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/kits.yml");
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
            config.set(key + ".disclaimer", kit.getDisclaimer());
            saveKitSettings(config, key, kit);
            Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFile("storage/kits.yml"), config);
        }
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
            applyDefaultSettings(config, key, kit);
            return;
        }

        for (String settingName : settingsSection.getKeys(false)) {
            String settingKey = key + ".settings." + settingName;
            String description = config.getString(settingKey + ".description");
            boolean enabled = config.getBoolean(settingKey + ".enabled");

            KitSetting kitSetting = Alley.getInstance().getKitSettingRepository().createSettingByName(settingName);
            if (kitSetting != null) {
                kitSetting.setDescription(description);
                kitSetting.setEnabled(enabled);
                kit.addKitSetting(kitSetting);
            }
        }
    }

    /**
     * Handle creation in config for each kit that has missing settings.
     *
     * @param kit    The kit.
     * @param config The configuration file.
     * @param key    The path key.
     */
    private void addMissingKitSettings(Kit kit, FileConfiguration config, String key) {
        Alley.getInstance().getKitSettingRepository().getSettings().forEach(setting -> {
            if (kit.getKitSettings().stream().noneMatch(kitSetting -> kitSetting.getName().equals(setting.getName()))) {
                kit.addKitSetting(setting);
                Bukkit.getConsoleSender().sendMessage(CC.translate("&cAdded missing setting " + setting.getName() + " to kit " + kit.getName() + ". Now saving it into the kits config..."));
                saveKitSettings(config, key, kit);
            }
        });
    }

    /**
     * Method to save a kit to the kits.yml file.
     *
     * @param kit The kit to save.
     */
    public void saveKit(Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/kits.yml");
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
        config.set(key + ".disclaimer", kit.getDisclaimer());

        if (kit.getKitSettings() == null) {
            applyDefaultSettings(config, key, kit);
        } else {
            saveKitSettings(config, key, kit);
        }

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFile("storage/kits.yml"), config);
    }

    /**
     * Method to apply the default settings to a kit.
     *
     * @param config The configuration file.
     * @param key    The path key.
     * @param kit    The kit.
     */
    public void applyDefaultSettings(FileConfiguration config, String key, Kit kit) {
        Alley.getInstance().getKitSettingRepository().getSettings().forEach(setting -> {
            kit.addKitSetting(setting);
            String settingKey = key + ".settings." + setting.getName();
            config.set(settingKey + ".description", setting.getDescription());
            config.set(settingKey + ".enabled", setting.isEnabled());
        });

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFile("storage/kits.yml"), config);
    }

    /**
     * Method to add a kit to the queue.
     *
     * @param kit The kit to add.
     */
    private void addKitToQueue(Kit kit) {
        if (!kit.isEnabled()) return;
        new Queue(kit, false);

        if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
            new Queue(kit, true);
        }
    }

    //deletes a kit
    /**
     * Method to delete a kit.
     *
     * @param kit The kit to delete.
     */
    public void deleteKit(Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfig("storage/kits.yml");
        File file = Alley.getInstance().getConfigHandler().getConfigFile("storage/kits.yml");

        kits.remove(kit);
        config.set("kits." + kit.getName(), null);

        Alley.getInstance().getConfigHandler().saveConfig(file, config);
    }

    /**
     * Method to create a kit.
     *
     * @param kitName  The name of the kit.
     * @param inventory The inventory of the kit.
     * @param armor     The armor of the kit.
     * @param icon      The icon of the kit.
     * @return The created kit.
     */
    public Kit createKit(String kitName, ItemStack[] inventory, ItemStack[] armor, Material icon) {
        Kit kit = new Kit(
                kitName,
                ConfigHandler.getInstance().getSettingsConfig().getString("kit.default-name").replace("{kit-name}", kitName),
                ConfigHandler.getInstance().getSettingsConfig().getString("kit.default-description").replace("{kit-name}", kitName),
                true,
                0,
                0,
                0,
                inventory,
                armor,
                icon,
                (byte) 0,
                kitName + " kit disclaimer."
        );

        Alley.getInstance().getKitSettingRepository().applyAllSettingsToKit(kit);

        return kit;
    }

    /**
     * Method to get a kit by name.
     *
     * @param name The name of the kit.
     * @return The kit.
     */
    public Kit getKit(String name) {
        return kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}