package me.emmy.alley.locale;

import lombok.Getter;
import me.emmy.alley.Alley;
import net.md_5.bungee.api.ChatColor;

import java.text.MessageFormat;

/**
 * Created by Emmy
 * Project: PluginBase
 * Date: 19/04/2024 - 17:41
 */

@Getter
public enum ConfigLocale {

    NO_PERM("messages.yml", "no-permission"),
    KIT_NOT_FOUND("messages.yml", "kit.not-found"),
    KIT_CREATED("messages.yml", "kit.created"),
    KIT_DELETED("messages.yml", "kit.deleted"),
    KIT_GIVEN("messages.yml", "kit.given");

    private final String configName, configString;

    ConfigLocale(String configName, String configString) {
        this.configName = configName;
        this.configString = configString;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&',
                Alley.getInstance().getConfig(configName).getString(configString));
    }
}
