package dev.revere.alley.locale.impl;

import lombok.Getter;
import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @date 19/04/2024 - 17:41
 */
@Getter
public enum PartyLocale {
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

    ;

    private final String configName, configString;

    /**
     * Constructor for the PartyLocale class.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    PartyLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(this.configName).getString(this.configString));
    }
}