package dev.revere.alley.base.kit.service;

import dev.revere.alley.base.kit.IKitService;
import dev.revere.alley.base.kit.Kit;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRaidingImpl;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
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
@Service(provides = IBaseRaidingService.class, priority = 100)
public class BaseRaidingService implements IBaseRaidingService {
    private final IKitService kitService;
    private final IConfigService configService;

    private final Map<String, Map<EnumBaseRaiderRole, Kit>> raidingKitMappings = new HashMap<>();

    /**
     * Constructor for DI.
     */
    public BaseRaidingService(IKitService kitService, IConfigService configService) {
        this.kitService = kitService;
        this.configService = configService;
    }

    @Override
    public void initialize(AlleyContext context) {
        this.loadRaidingKitMappings();
    }

    public void loadRaidingKitMappings() {
        this.raidingKitMappings.clear();
        FileConfiguration config = this.configService.getKitsConfig();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection == null) {
            Logger.warn("Could not find 'kits' section in kits.yml for RaidingService.");
            return;
        }

        for (Kit kit : this.kitService.getKits()) {
            if (kit.isSettingEnabled(KitSettingRaidingImpl.class)) {
                String key = "kits." + kit.getName();
                this.loadRaidingKitMapping(kit, config, key);
            }
        }
    }
    private void loadRaidingKitMapping(Kit parentKit, FileConfiguration config, String key) {
        ConfigurationSection raidingSection = config.getConfigurationSection(key + ".raiding-role-kits");
        if (raidingSection == null) {
            return;
        }

        Map<EnumBaseRaiderRole, Kit> roleKits = new HashMap<>();
        for (String roleName : raidingSection.getKeys(false)) {
            try {
                EnumBaseRaiderRole role = EnumBaseRaiderRole.valueOf(roleName.toUpperCase());
                String roleKitName = raidingSection.getString(roleName);

                Kit roleKit = this.kitService.getKit(roleKitName);

                if (roleKit != null) {
                    roleKits.put(role, roleKit);
                    roleKit.setEnabled(false);
                } else {
                    Logger.error("Raiding sub-kit for role " + role + " not found: " + roleKitName);
                }
            } catch (IllegalArgumentException e) {
                Logger.error("Invalid raiding role: '" + roleName + "' in parent kit: " + parentKit.getName());
            }
        }

        if (!roleKits.isEmpty()) {
            this.raidingKitMappings.put(parentKit.getName(), roleKits);
        }
    }

    @Override
    public void setRaidingKitMapping(Kit parentKit, EnumBaseRaiderRole role, Kit roleKit) {
        this.raidingKitMappings.computeIfAbsent(parentKit.getName(), k -> new HashMap<>()).put(role, roleKit);

        FileConfiguration config = this.configService.getKitsConfig();
        String key = "kits." + parentKit.getName() + ".raiding-role-kits." + role.name().toLowerCase();
        config.set(key, roleKit.getName());

        this.kitService.saveKit(parentKit);
    }

    @Override
    public void removeRaidingKitMapping(Kit parentKit, EnumBaseRaiderRole role) {
        Map<EnumBaseRaiderRole, Kit> roleKits = this.raidingKitMappings.get(parentKit.getName());
        if (roleKits == null || !roleKits.containsKey(role)) {
            return;
        }

        roleKits.remove(role);
        if (roleKits.isEmpty()) {
            this.raidingKitMappings.remove(parentKit.getName());
        }

        FileConfiguration config = this.configService.getKitsConfig();
        String key = "kits." + parentKit.getName() + ".raiding-role-kits." + role.name().toLowerCase();
        config.set(key, null);

        this.kitService.saveKit(parentKit);
    }

    @Override
    public Kit getRaidingKitByRole(Kit parentKit, EnumBaseRaiderRole role) {
        return this.raidingKitMappings.getOrDefault(parentKit.getName(), new HashMap<>()).get(role);
    }

    @Override
    public Kit getRaidingKit() {
        return this.kitService.getKits().stream()
                .filter(kit -> kit.isSettingEnabled(KitSettingRaidingImpl.class))
                .findFirst()
                .orElse(null);
    }

    public Kit getRaidingKitByRole(EnumBaseRaiderRole role) {
        Kit primaryKit = this.getRaidingKit();
        if (primaryKit == null) {
            return null;
        }
        return this.getRaidingKitByRole(primaryKit, role);
    }

    @Override
    public boolean hasRaidingKit(Kit kit) {
        return this.raidingKitMappings.containsKey(kit.getName());
    }
}