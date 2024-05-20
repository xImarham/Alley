package me.emmy.alley.kit;

import lombok.Getter;
import me.emmy.alley.Alley;
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
public class KitManager {
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

            kits.add(kit);
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

        }
        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml"), config);
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

        Alley.getInstance().getConfigHandler().saveConfig(Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml"), config);
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
