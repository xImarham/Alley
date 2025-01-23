package dev.revere.alley.locale;

import lombok.Getter;
import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:41
 */

@Getter
public enum Locale {

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

    PARTY_CREATED("messages.yml", "party.created"),
    PARTY_DISBANDED("messages.yml", "party.disbanded"),
    NOT_IN_PARTY("messages.yml", "party.not-in-party"),
    NOT_LEADER("messages.yml", "party.not-leader"),
    ALREADY_IN_PARTY("messages.yml", "party.already-in-party"),
    PARTY_LEFT("messages.yml", "party.left"),
    PLAYER_DISABLED_PARTY_INVITES("messages.yml", "party.target-disabled-invites"),
    DISABLED_PARTY_CHAT("messages.yml", "party.disabled-chat"),
    NO_PARTY_INVITE("messages.yml", "party.no-invite"),
    JOINED_PARTY("messages.yml", "party.joined"),

    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard"),
    TOGGLE_TABLIST("messages.yml", "player-settings.tablist")
    ;

    private final String configName, configString;

    /**
     * Constructor for the Locale class.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    Locale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(configName).getString(configString));
    }
}
