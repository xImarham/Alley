package dev.revere.alley.feature.kit;

import dev.revere.alley.feature.kit.enums.EnumKitCategory;
import dev.revere.alley.feature.kit.setting.KitSetting;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @date 28/04/2024 - 21:49
 */
@Getter
@Setter
public class Kit {
    private String name;
    private String displayName;
    private String description;
    private String disclaimer;
    private String ffaArenaName;

    private boolean enabled;
    private boolean ffaEnabled;

    private int unrankedSlot;
    private int rankedSlot;
    private int editorSlot;
    private int ffaSlot;

    private int maxFfaPlayers;

    private ItemStack[] inventory;
    private ItemStack[] armor;

    private Material icon;
    private int durability;

    private EnumKitCategory category;

    private List<PotionEffect> potionEffects;
    private final List<KitSetting> kitSettings;

    /**
     * Constructor for the Kit class.
     *
     * @param name         The name of the kit.
     * @param displayName  The display name of the kit.
     * @param description  The description of the kit.
     * @param enabled      Whether the kit is enabled.
     * @param unrankedSlot The unranked slot of the kit.
     * @param rankedSlot   The ranked slot of the kit.
     * @param editorSlot   The editor slot of the kit.
     * @param inventory    The inventory of the kit.
     * @param armor        The armor of the kit.
     * @param icon         The icon of the kit.
     * @param durability     The icon data of the kit.
     * @param disclaimer   The disclaimer of the kit.
     */
    public Kit(String name, String displayName, String description, boolean enabled, int unrankedSlot, int rankedSlot, int editorSlot, int ffaSlot, ItemStack[] inventory, ItemStack[] armor, Material icon, int durability, String disclaimer, EnumKitCategory category) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.enabled = enabled;
        this.unrankedSlot = unrankedSlot;
        this.rankedSlot = rankedSlot;
        this.editorSlot = editorSlot;
        this.inventory = inventory;
        this.armor = armor;
        this.icon = icon;
        this.durability = durability;
        this.kitSettings = new ArrayList<>();
        this.disclaimer = disclaimer;
        this.potionEffects = new ArrayList<>();
        this.ffaEnabled = false;
        this.ffaSlot = ffaSlot;
        this.ffaArenaName = "";
        this.maxFfaPlayers = 20;
        this.category = category;
    }

    /**
     * Method to add a kit setting.
     *
     * @param kitSetting The kit setting to add.
     */
    public void addKitSetting(KitSetting kitSetting) {
        this.kitSettings.add(kitSetting);
    }

    /**
     * Method to check if a setting is enabled.
     *
     * @param name The name of the setting.
     * @return Whether the setting is enabled.
     */
    public boolean isSettingEnabled(String name) {
        KitSetting kitSetting = this.kitSettings.stream()
                .filter(setting -> setting.getName().equals(name))
                .findFirst()
                .orElse(null);

        return kitSetting != null && kitSetting.isEnabled();
    }

    /**
     * Method to check if a setting is enabled.
     *
     * @param clazz The class of the setting.
     * @return Whether the setting is enabled.
     */
    public boolean isSettingEnabled(Class<? extends KitSetting> clazz) {
        KitSetting kitSetting = this.kitSettings.stream()
                .filter(setting -> setting.getClass().equals(clazz))
                .findFirst()
                .orElse(null);

        return kitSetting != null && kitSetting.isEnabled();
    }

    /**
     * Method to apply the potion effects of the kit to a player.
     *
     * @param player The player to apply the potion effects to.
     */
    public void applyPotionEffects(Player player) {
        for (PotionEffect effect : this.potionEffects) {
            player.addPotionEffect(effect);
        }
    }
}