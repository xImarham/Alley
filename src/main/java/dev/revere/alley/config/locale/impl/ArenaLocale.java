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

    ALREADY_EXISTS("messages.yml", "arena.error.already-exists"),
    NOT_FOUND("messages.yml", "arena.error.not-found"),

    SELECTION_TOOL_ADDED("messages.yml", "arena.manage.selection-tool.added"),
    SELECTION_TOOL_REMOVED("messages.yml", "arena.manage.selection-tool.removed"),

    CREATED("messages.yml", "arena.manage.created"),
    DELETED("messages.yml", "arena.manage.deleted"),

    SAVED_ALL("messages.yml", "arena.storage.saved-all"),

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