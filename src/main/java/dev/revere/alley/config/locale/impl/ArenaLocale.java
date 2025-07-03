package dev.revere.alley.config.locale.impl;

import dev.revere.alley.Alley;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.config.locale.ILocale;
import dev.revere.alley.util.chat.CC;

/**
 * @author Emmy
 * @project Alley
 * @since 26/06/2025
 */
public enum ArenaLocale implements ILocale {

    TEST("arena.test", "test"),

    ;


    private final String configName;
    private final String configString;

    /**
     * Constructor for the ArenaLocale enum.
     *
     * @param configName   The name of the config.
     * @param configString The string of the config.
     */
    ArenaLocale(String configName, String configString) {
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
        return CC.translate(Alley.getInstance().getService(IConfigService.class).getConfig(this.configName).getString(this.configString));
    }
}