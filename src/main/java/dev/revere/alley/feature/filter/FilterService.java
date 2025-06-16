package dev.revere.alley.feature.filter;

import dev.revere.alley.Alley;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Emmy
 * @project Alley
 * @since 27/04/2025
 */
@Getter
public class FilterService {
    protected final Alley plugin;
    private final Set<String> filteredWords;

    private final String notificationMessage;

    /**
     * Constructor for the FilterService class.
     *
     * @param plugin The Alley plugin instance.
     */
    public FilterService(Alley plugin) {
        this.plugin = plugin;
        this.filteredWords = new HashSet<>();
        this.notificationMessage = plugin.getConfigService().getSettingsConfig().getString("profanity-filter.staff-notification-format");
        this.loadFilteredWords();
    }

    private void loadFilteredWords() {
        FileConfiguration config = this.plugin.getConfigService().getSettingsConfig();
        ConfigurationSection section = config.getConfigurationSection("profanity-filter");

        if (section != null) {
            List<String> configFilteredWords = section.getStringList("filtered-words");
            this.filteredWords.addAll(configFilteredWords);
        }

        if (config.getBoolean("profanity-filter.add-default-words")) {
            this.filteredWords.addAll(this.getDefaultFilteredWords());
        }
    }

    /**
     * Censors words in the message based on the filtered words list.
     *
     * @param message The message to censor.
     * @return The censored message.
     */
    public String censorWords(String message) {
        String censoredMessage = message;
        String[] words = message.split(" ");

        for (String word : words) {
            String normalizedWord = this.normalize(word);
            if (this.filteredWords.contains(normalizedWord)) {
                censoredMessage = censoredMessage.replaceAll("(?i)" + word, "****");
            }
        }

        return censoredMessage;
    }

    /**
     * Checks if a message contains any profane words.
     *
     * @param message The message to check.
     * @return True if the message contains profane words, false otherwise.
     */
    public boolean isProfanity(String message) {
        String[] words = message.split(" ");

        for (String word : words) {
            String normalizedWord = this.normalize(word);
            if (this.filteredWords.contains(normalizedWord)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Notifies staff members about a rude word detected in a player's message.
     *
     * @param message  The message containing the rude word.
     * @param insulter The player who sent the message.
     */
    public void notifyStaff(String message, Player insulter) {
        String permission = this.plugin.getPluginConstant().getAdminPermissionPrefix();
        String replacedMessage = this.notificationMessage.replace("{player}", insulter.getName()).replace("{message}", message);

        this.plugin.getServer().getOnlinePlayers().stream().filter(player ->
                player.hasPermission(permission)).forEach(player ->
                player.sendMessage(CC.translate(replacedMessage)))
        ;

        Bukkit.getConsoleSender().sendMessage(CC.translate(replacedMessage)); // idk why, but i felt like its necessary to send it to console too
    }

    /**
     * Normalizes words or messages by replacing certain characters with their corresponding letters.
     * This ensures that words like "b1tch" or "sh!t" are properly normalized.
     *
     * @param word The word or message to normalize.
     * @return The normalized version of the input.
     */
    private String normalize(String word) {
        return word.toLowerCase()
                .replace("@", "a")
                .replace("0", "o")
                .replace("1", "i")
                .replace("3", "e")
                .replace("5", "s")
                .replace("7", "t")
                .replace("8", "b")
                .replace("!", "i")
                .replaceAll("\\p{Punct}|\\d", "")
                .trim();
    }

    private List<String> getDefaultFilteredWords() {
        return Arrays.asList(
                "fuck", "shit", "bitch", "asshole", "bastard", "dick", "pussy", "cunt",
                "fag", "faggot", "slut", "whore", "retard", "nigger", "nigga", "chink",
                "spic", "kike", "twat", "cock", "motherfucker", "douche", "dumbass", "dipshit",
                "jackass", "prick", "cocksucker", "fucker", "shithead", "anus", "arse", "bollocks",
                "bugger", "wanker", "tosser"

                // ^ all copilot completion by the way, not me.
        );
    }
}