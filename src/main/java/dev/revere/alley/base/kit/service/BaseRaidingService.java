package dev.revere.alley.base.kit.service;

import dev.revere.alley.Alley;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.game.match.player.enums.EnumBaseRaiderRole;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Remi
 * @project alley-practice
 * @date 20/06/2025
 */
@Getter
public class BaseRaidingService {
    protected final Alley plugin;
    private final Map<String, Map<EnumBaseRaiderRole, Kit>> raidingKitMappings;

    /**
     * Constructor for the BaseRaidingService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public BaseRaidingService(Alley plugin) {
        this.plugin = plugin;
        this.raidingKitMappings = new HashMap<>();
        this.loadRaidingKitMappings();
    }

    /**
     * Load raiding kit mappings from the configuration file.
     * This method reads the "kits" section of the configuration and populates the raidingKitMappings map.
     */
    public void loadRaidingKitMappings() {
        FileConfiguration config = this.plugin.getConfigService().getKitsConfig();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            Logger.logError("No raiding kits found in the configuration.");
            return;
        }

        for (String kitName : kitsSection.getKeys(false)) {
            String key = "kits." + kitName;
            Kit kit = this.plugin.getKitService().getKit(kitName);

            if (kit != null && kit.isSettingEnabled(KitSettingRaidingImpl.class)) {
               this.loadRaidingKitMapping(kit, config, key);
            }
        }
    }

    /**
     * Load raiding kit mappings from the configuration for a specific parent kit.
     *
     * @param parentKit the parent kit to load raiding kits for
     * @param config    the configuration file containing the raiding kit mappings
     * @param key       the key in the configuration where raiding kits are defined
     */
    private void loadRaidingKitMapping(Kit parentKit, FileConfiguration config, String key) {
        ConfigurationSection raidingSection = config.getConfigurationSection(key + ".raiding-role-kits");
        if (raidingSection == null) {
            Logger.logError("No raiding role kits found for kit: " + parentKit.getName());
            return;
        }

        Map<EnumBaseRaiderRole, Kit> roleKits = new HashMap<>();
        for (String roleName : raidingSection.getKeys(false)) {
            try {
                EnumBaseRaiderRole role = EnumBaseRaiderRole.valueOf(roleName.toUpperCase());
                String roleKitName = raidingSection.getString(roleName);
                Kit roleKit = this.plugin.getKitService().getKit(roleKitName);

                if (roleKit != null) {
                    roleKits.put(role, roleKit);
                    roleKit.setEnabled(false);
                } else {
                    Logger.logError("Raiding kit for role " + role + " not found: " + roleKitName);
                }
            }
            catch (IllegalArgumentException e) {
                Logger.logError("Invalid raiding role: " + roleName + " in kit: " + parentKit.getName());
            }
        }

        if (!roleKits.isEmpty()) {
            this.raidingKitMappings.put(parentKit.getName(), roleKits);
        }
    }

    public void setRaidingKitMapping(Kit parentKit, EnumBaseRaiderRole role, Kit roleKit) {
        this.raidingKitMappings.computeIfAbsent(parentKit.getName(), k -> new HashMap<>()).put(role, roleKit);

        FileConfiguration config = this.plugin.getConfigService().getKitsConfig();
        String key = "kits." + parentKit.getName() + ".raiding-role-kits." + role.name().toLowerCase();
        config.set(key, roleKit.getName());

        this.plugin.getKitService().saveKit(parentKit);
    }

    public void removeRaidingKitMapping(EnumBaseRaiderRole role) {
        for (Map.Entry<String, Map<EnumBaseRaiderRole, Kit>> entry : this.raidingKitMappings.entrySet()) {
            Map<EnumBaseRaiderRole, Kit> roleKits = entry.getValue();
            if (roleKits.containsKey(role)) {
                roleKits.remove(role);
                if (roleKits.isEmpty()) {
                    this.raidingKitMappings.remove(entry.getKey());
                }
            }
        }

        FileConfiguration config = this.plugin.getConfigService().getKitsConfig();
        for (String kitName : this.raidingKitMappings.keySet()) {
            String key = "kits." + kitName + ".raiding-role-kits." + role.name().toLowerCase();
            config.set(key, null);
        }
    }

    /**
     * Get the raiding kit for a specific role.
     *
     * @param role the raider role
     * @return the raiding kit for the specified role, or null if not found
     */
    public Kit getRaidingKitByRole(EnumBaseRaiderRole role) {
        Kit primaryKit = this.getRaidingKit();
        if (primaryKit == null) {
            return null;
        }

        return this.getRaidingKitByRole(primaryKit, role);
    }

    /**
     * Get the raiding kit for a specific role within a parent kit.
     *
     * @param parentKit the parent kit
     * @param role      the raider role
     * @return the raiding kit for the specified role, or null if not found
     */
    public Kit getRaidingKitByRole(Kit parentKit, EnumBaseRaiderRole role) {
        Map<EnumBaseRaiderRole, Kit> roleKits = this.raidingKitMappings.get(parentKit.getName());
        if (roleKits == null) {
            return null;
        }
        return roleKits.get(role);
    }

    /**
     * Get the raiding kit for the first kit that has a raiding kit mapping.
     *
     * @return the first raiding kit found, or null if none exist
     */
    public Kit getRaidingKit() {
        return this.plugin.getKitService().getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRaidingImpl.class))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if the given kit has a raiding kit mapping.
     *
     * @param kit the kit to check
     * @return true if the kit has a raiding kit mapping, false otherwise
     */
    public boolean hasRaidingKit(Kit kit) {
        return this.raidingKitMappings.containsKey(kit.getName());
    }
}