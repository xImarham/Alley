package dev.revere.alley.feature.kit;

import dev.revere.alley.Alley;
import dev.revere.alley.game.ffa.AbstractFFAMatch;
import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.feature.kit.settings.KitSetting;
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
    private final List<KitSetting> kitSettings;

    private String name;
    private String displayName;
    private String description;

    private boolean enabled;

    private int unrankedslot;
    private int rankedslot;
    private int editorslot;

    private ItemStack[] inventory;
    private ItemStack[] armor;

    private Material icon;
    private int iconData;

    private String disclaimer;

    private List<PotionEffect> potionEffects;

    /**
     * Constructor for the Kit class.
     *
     * @param name         The name of the kit.
     * @param displayName  The display name of the kit.
     * @param description  The description of the kit.
     * @param enabled      Whether the kit is enabled.
     * @param unrankedslot The unranked slot of the kit.
     * @param rankedslot   The ranked slot of the kit.
     * @param editorslot   The editor slot of the kit.
     * @param inventory    The inventory of the kit.
     * @param armor        The armor of the kit.
     * @param icon         The icon of the kit.
     * @param iconData     The icon data of the kit.
     */
    public Kit(String name, String displayName, String description, boolean enabled, int unrankedslot, int rankedslot, int editorslot, ItemStack[] inventory, ItemStack[] armor, Material icon, int iconData, String disclaimer) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.enabled = enabled;
        this.unrankedslot = unrankedslot;
        this.rankedslot = rankedslot;
        this.editorslot = editorslot;
        this.inventory = inventory;
        this.armor = armor;
        this.icon = icon;
        this.iconData = iconData;
        this.kitSettings = new ArrayList<>();
        this.disclaimer = disclaimer;
        this.potionEffects = new ArrayList<>();
    }

    /**
     * Method to add a kit setting.
     *
     * @param kitSetting The kit setting to add.
     */
    public void addKitSetting(KitSetting kitSetting) {
        kitSettings.add(kitSetting);
    }

    /**
     * Method to check if a setting is enabled.
     *
     * @param name The name of the setting.
     * @return Whether the setting is enabled.
     */
    public boolean isSettingEnabled(String name) {
        KitSetting kitSetting = kitSettings.stream()
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
        KitSetting kitSetting = kitSettings.stream()
                .filter(setting -> setting.getClass().equals(clazz))
                .findFirst()
                .orElse(null);

        return kitSetting != null && kitSetting.isEnabled();
    }

    /**
     * Method to check if multiple settings are enabled.
     *
     * @param names The names of the settings.
     * @return Whether the settings are enabled.
     */
    public boolean areSettingsEnabled(String... names) {
        for (String name : names) {
            if (!this.isSettingEnabled(name)) {
                return false;
            }
        }
        return true;
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

    /**
     * Method to check if the kit is a FFA kit.
     *
     * @return Whether the kit is a FFA kit.
     */
    public boolean isFfaKit() {
        for (AbstractFFAMatch match : Alley.getInstance().getFfaService().getMatches()) {
            if (match.getKit().getName().equals(this.name)) {
                return true;
            }
        }
        return false;
    }
}