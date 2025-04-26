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
public enum ProfileLocale implements ILocale {
    TOGGLED_PARTY_INVITES("messages.yml", "player-settings.party-invites"),
    TOGGLED_PARTY_MESSAGES("messages.yml", "player-settings.party-messages"),
    TOGGLED_SCOREBOARD("messages.yml", "player-settings.scoreboard"),
    TOGGLED_SCOREBOARD_LINES("messages.yml", "player-settings.scoreboard-lines"),
    TOGGLE_TABLIST("messages.yml", "player-settings.tablist");

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