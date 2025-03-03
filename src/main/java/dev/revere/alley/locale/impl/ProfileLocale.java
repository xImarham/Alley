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
public enum ProfileLocale {
    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard"),
    TOGGLE_TABLIST("messages.yml", "player-settings.tablist")

    ;

    private final String configName, configString;

    /**
     * Constructor for the ProfileLocale class.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ProfileLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return CC.translate(Alley.getInstance().getConfigService().getConfig(this.configName).getString(this.configString));
    }
}