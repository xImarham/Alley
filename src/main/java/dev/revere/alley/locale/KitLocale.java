package dev.revere.alley.locale;

import dev.revere.alley.Alley;
import dev.revere.alley.api.locale.ILocale;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 03/03/2025
 */
@Getter
public enum KitLocale implements ILocale {
    KIT_NOT_FOUND("messages.yml", "kit.not-found"),
    KIT_ALREADY_EXISTS("messages.yml", "kit.already-exists"),
    KIT_SAVED("messages.yml", "kit.saved"),
    KIT_SAVED_ALL("messages.yml", "kit.saved-all"),
    KIT_CREATED("messages.yml", "kit.created"),
    KIT_DELETED("messages.yml", "kit.deleted"),
    KIT_INVENTORY_GIVEN("messages.yml", "kit.inventory-given"),
    KIT_INVENTORY_SET("messages.yml", "kit.inventory-set"),
    KIT_DESCRIPTION_SET("messages.yml", "kit.description-set"),
    KIT_DISCLAIMER_SET("messages.yml", "kit.disclaimer-set"),
    KIT_DISPLAYNAME_SET("messages.yml", "kit.displayname-set"),
    KIT_ICON_SET("messages.yml", "kit.icon-set"),
    KIT_EDITORSLOT_SET("messages.yml", "kit.editorslot-set"),
    KIT_UNRANKEDSLOT_SET("messages.yml", "kit.unrankedslot-set"),
    KIT_RANKEDSLOT_SET("messages.yml", "kit.rankedslot-set"),
    KIT_SETTING_SET("messages.yml", "kit.setting-set"),
    KIT_CANNOT_SET_IN_CREATIVE("messages.yml", "kit.cannot-set-in-creative"),

    ;

    private final String configName, configString;

    /**
     * Constructor for the KitLocale class.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    KitLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    /**
     * Gets the String from the config.
     *
     * @return The message from the config.
     */
    @Override
    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(this.configName).getString(this.configString));
    }
}