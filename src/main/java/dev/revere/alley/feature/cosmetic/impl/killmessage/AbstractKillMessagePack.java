package dev.revere.alley.feature.cosmetic.impl.killmessage;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.cosmetic.AbstractCosmetic;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project alley-practice
 * @date 27/06/2025
 */
public abstract class AbstractKillMessagePack extends AbstractCosmetic {
    private final Map<EntityDamageEvent.DamageCause, List<String>> messagesByCause = new EnumMap<>(EntityDamageEvent.DamageCause.class);

    public AbstractKillMessagePack() {
        super();
        this.loadMessages();
    }

    /**
     * Concrete classes must implement this to specify which .yml file to load.
     *
     * @return The name of the resource file (e.g., "salty_messages.yml").
     */
    protected abstract String getResourceFileName();

    private void loadMessages() {
        String fileName = getResourceFileName();
        String configPath = "cosmetics/messages/" + fileName;

        FileConfiguration config = Alley.getInstance().getConfigService().getConfig(configPath);

        if (config == null) {
            Logger.logError("Could not load kill message config: " + configPath);
            Logger.logError("Make sure the file is added to ConfigService.configFileNames array!");
            return;
        }

        try {
            for (String key : config.getKeys(false)) {
                try {
                    EntityDamageEvent.DamageCause cause = EntityDamageEvent.DamageCause.valueOf(key.toUpperCase());
                    this.messagesByCause.put(cause, config.getStringList(key));
                } catch (IllegalArgumentException e) {
                    if (key.equalsIgnoreCase("GENERIC")) {
                        this.messagesByCause.put(null, config.getStringList(key));
                    } else {
                        Logger.logError("Unknown damage cause in " + fileName + ": " + key);
                    }
                }
            }

            Logger.log("Loaded kill message pack: " + fileName);
        } catch (Exception e) {
            Logger.logException("Failed to load kill message pack: " + fileName, e);
        }
    }

    /**
     * Gets a random message for a given damage cause, with fallback to GENERIC.
     *
     * @param cause The cause of death.
     * @return A random message string, or null if none are available.
     */
    public String getRandomMessage(EntityDamageEvent.DamageCause cause) {
        List<String> messageList = messagesByCause.get(cause);

        if (messageList == null || messageList.isEmpty()) {
            messageList = messagesByCause.get(null);
        }

        if (messageList == null || messageList.isEmpty()) {
            return null;
        }

        return messageList.get(ThreadLocalRandom.current().nextInt(messageList.size()));
    }

    /**
     * Gathers all message strings from all categories into a single list.
     */
    public List<String> getDisplayableMessages() {
        return this.messagesByCause.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getDisplayLore() {
        List<String> lore = new ArrayList<>();
        for (String message : getDisplayableMessages()) {
            lore.add("&f- &6" + message.replace("{victim}", "victim").replace("{killer}", "killer"));
        }

        if (lore.isEmpty()) {
            lore.add("&7" + this.getDescription());
        }

        return lore;
    }

    @Override
    public void execute(Player player) {

    }
}
