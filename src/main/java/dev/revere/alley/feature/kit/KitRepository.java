package dev.revere.alley.feature.kit;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.KitSetting;
import dev.revere.alley.feature.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.feature.queue.Queue;
import dev.revere.alley.util.PotionUtil;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

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
    private final List<Kit> kits;
    private final Alley plugin;

    /**
     * Constructor for the KitRepository class.
     *
     * @param plugin The main class.
     */
    public KitRepository(Alley plugin) {
        this.kits = new ArrayList<>();
        this.plugin = plugin;
        this.loadKits();
    }

    /**
     * Method to load all kits from the kits.yml file.
     */
    public void loadKits() {
        FileConfiguration config = Alley.getInstance().getConfigService().getConfig("storage/kits.yml");
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

            this.loadKitSettings(config, key, kit);
            this.loadPotionEffects(config, key, kit);
            this.addMissingKitSettings(kit, config, key);
            this.kits.add(kit);
            this.addKitToQueue(kit);
        }
    }

    /**
     * Method to save all kits to the kits.yml file.
     */
    public void saveKits() {
        for (Kit kit : this.kits) {
            FileConfiguration config = Alley.getInstance().getConfigService().getKitsConfig();
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
            this.saveKitSettings(config, key, kit);
            this.savePotionEffects(config, key, kit);
            Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/kits.yml"), config);
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
     * Method to save the potion effects of a kit.
     *
     * @param config The configuration file.
     * @param key    The path key.
     * @param kit    The kit.
     */
    private void savePotionEffects(FileConfiguration config, String key, Kit kit) {
        List<String> potionEffects = PotionUtil.serialize(kit.getPotionEffects());
        config.set(key + ".potioneffects", potionEffects);
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
            this.applyDefaultSettings(config, key, kit);
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
     * Method to load the potion effects of a kit.
     *
     * @param config The configuration file.
     * @param key    The path key.
     * @param kit    The kit.
     */
    private void loadPotionEffects(FileConfiguration config, String key, Kit kit) {
        try {
            List<PotionEffect> potionEffects = PotionUtil.deserialize(config.getStringList(key + ".potioneffects"));
            kit.setPotionEffects(potionEffects);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&cFailed to load potion effects for kit " + kit.getName() + "."));
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
                this.saveKitSettings(config, key, kit);
            }
        });
    }

    /**
     * Method to save a kit to the kits.yml file.
     *
     * @param kit The kit to save.
     */
    public void saveKit(Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigService().getKitsConfig();
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
            this.applyDefaultSettings(config, key, kit);
        } else {
            this.saveKitSettings(config, key, kit);
        }

        if (kit.getPotionEffects() != null) {
            this.savePotionEffects(config, key, kit);
        }

        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/kits.yml"), config);
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

        Alley.getInstance().getConfigService().saveConfig(Alley.getInstance().getConfigService().getConfigFile("storage/kits.yml"), config);
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
        FileConfiguration config = Alley.getInstance().getConfigService().getKitsConfig();
        File file = Alley.getInstance().getConfigService().getConfigFile("storage/kits.yml");

        this.kits.remove(kit);
        config.set("kits." + kit.getName(), null);

        Alley.getInstance().getConfigService().saveConfig(file, config);
    }

    /**
     * Method to create a kit.
     *
     * @param kitName   The name of the kit.
     * @param inventory The inventory of the kit.
     * @param armor     The armor of the kit.
     * @param icon      The icon of the kit.
     */
    public void createKit(String kitName, ItemStack[] inventory, ItemStack[] armor, Material icon, int slot) {
        Kit kit = new Kit(
                kitName,
                Alley.getInstance().getConfigService().getSettingsConfig().getString("kit.default-name").replace("{kit-name}", kitName),
                Alley.getInstance().getConfigService().getSettingsConfig().getString("kit.default-description").replace("{kit-name}", kitName),
                true,
                slot,
                0,
                0,
                inventory,
                armor,
                icon,
                (byte) 0,
                kitName + " kit disclaimer."
        );

        Alley.getInstance().getKitSettingRepository().applyAllSettingsToKit(kit);
        this.kits.add(kit);
        this.saveKit(kit);
    }

    /**
     * Method to get a kit by name.
     *
     * @param name The name of the kit.
     * @return The kit.
     */
    public Kit getKit(String name) {
        return this.kits.stream()
                .filter(kit -> kit.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}