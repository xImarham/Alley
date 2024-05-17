package me.emmy.alley.kit;

import lombok.Getter;
import me.emmy.alley.Alley;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Getter
public class KitManager {
    private final List<Kit> kits = new ArrayList<>();

    public Kit getKit(String name) {
        for (Kit kit : Alley.getInstance().getKitManager().getKits()) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public Kit loadKit(String kitName) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");
        String path = "kits." + kitName;

        if (config.contains(path)) {
            String displayName = config.getString(path + ".displayName");
            String description = config.getString(path + ".description");
            String name = config.getString(path + ".name");

            ItemStack[] armour = config.getList(path + ".armour").toArray(new ItemStack[0]);
            ItemStack[] items = config.getList(path + ".items").toArray(new ItemStack[0]);
            ItemStack icon = config.getItemStack(path + ".icon");

            List<String> arenas = config.getStringList(path + ".arenas");
            List<String> types = config.getStringList(path + ".types");

            return Kit.builder()
                    .displayName(displayName)
                    .description(description)
                    .name(name)
                    .items(items)
                    .armour(armour)
                    .icon(icon)
                    .arenas(arenas)
                    .types(types)
                    .build();
        }

        return null;
    }

    public void saveKit(String kitName, Kit kit) {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");
        File file = Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml");
        String path = "kits." + kitName;

        config.set(path + ".displayName", kit.getDisplayName());
        config.set(path + ".description", kit.getDescription());
        config.set(path + ".name", kit.getName());

        config.set(path + ".items", kit.getItems());
        config.set(path + ".armour", kit.getArmour());
        config.set(path + ".icon", kit.getIcon());

        config.set(path + ".arenas", kit.getArenas());
        config.set(path + ".types", kit.getTypes());

        Alley.getInstance().getConfigHandler().saveConfig(file, config);
    }

    public void saveKitsToFile() {
        File file = Alley.getInstance().getConfigHandler().getConfigFileByName("storage/kits.yml");
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");

        for (Kit kit : kits) {
            saveKit(kit.getName(), kit);
        }

        Alley.getInstance().getConfigHandler().saveConfig(file, config);
    }

    public List<Kit> loadKitsFromFile() {
        FileConfiguration config = Alley.getInstance().getConfigHandler().getConfigByName("storage/kits.yml");

        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection != null) {
            for (String kitName : kitsSection.getKeys(false)) {
                Kit kit = loadKit(kitName);
                if (kit != null) {
                    kits.add(kit);
                }
            }
        }

        return kits;
    }
}