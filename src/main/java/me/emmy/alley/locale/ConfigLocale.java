package me.emmy.alley.locale;

import lombok.Getter;
import me.emmy.alley.Alley;
import me.emmy.alley.utils.chat.CC;

/**
 * Created by Emmy
 * Project: Alley
 * Date: 19/04/2024 - 17:41
 */

@Getter
public enum ConfigLocale {

    NO_PERM("messages.yml", "no-permission"),

    KIT_NOT_FOUND("messages.yml", "kit.not-found"),
    KIT_SAVED("messages.yml", "kit.saved"),
    KIT_SAVED_ALL("messages.yml", "kit.saved-all"),
    KIT_CREATED("messages.yml", "kit.created"),
    KIT_DELETED("messages.yml", "kit.deleted"),
    KIT_INVENTORY_GIVEN("messages.yml", "kit.inventory-given"),
    KIT_INVENTORY_SET("messages.yml", "kit.inventory-set"),
    KIT_DESCRIPTION_SET("messages.yml", "kit.description-set"),
    KIT_EDITORSLOT_SET("messages.yml", "kit.editorslot-set"),
    KIT_UNRANKEDSLOT_SET("messages.yml", "kit.unrankedslot-set"),
    KIT_RANKEDSLOT_SET("messages.yml", "kit.rankedslot-set"),

    NOT_IN_PARTY("messages.yml", "party.not-in-party"),
    PARTY_CREATED("messages.yml", "party.created"),
    PARTY_DISBANDED("messages.yml", "party.disbanded"),
    PARTY_LEFT("messages.yml", "party.left")
    ;

    private final String configName, configString;

    ConfigLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfig(configName).getString(configString));
    }
}
