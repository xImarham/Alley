package dev.revere.alley.core;

import dev.revere.alley.core.enums.EnumCoreType;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Emmy
 * @project Alley
 * @since 26/04/2025
 */
public interface ICore {
    /**
     * Retrieves the plugin name of the core implementation.
     *
     * @return The plugin name as a String.
     */
    EnumCoreType getType();

    /**
     * Retrieves the color associated with a given player.
     *
     * @param player The player whose color is to be retrieved.
     * @return The color as a ChatColor object.
     */
    ChatColor getPlayerColor(Player player);

    /**
     * Retrieves the rank prefix for a given player.
     *
     * @param player The player whose rank prefix is to be retrieved.
     * @return The rank prefix as a String.
     */
    String getRankPrefix(Player player);

    /**
     * Retrieves the rank suffix for a given player.
     *
     * @param player The player whose rank suffix is to be retrieved.
     * @return The rank suffix as a String.
     */
    String getRankSuffix(Player player);

    /**
     * Retrieves the rank color for a given player.
     *
     * @param player The player whose rank color is to be retrieved.
     * @return The rank color as a ChatColor object.
     */
    ChatColor getRankColor(Player player);

    /**
     * Retrieves the tag prefix for a given player.
     *
     * @param player The player whose tag prefix is to be retrieved.
     * @return The tag prefix as a String.
     */
    String getTagPrefix(Player player);

    /**
     * Retrieves the color associated with a given player's tag.
     *
     * @param player The player whose tag color is to be retrieved.
     * @return The tag color as a String.
     */
    ChatColor getTagColor(Player player);

    /**
     * Retrieves the chat format for a given player.
     *
     * @param player       The player whose chat format is to be retrieved.
     * @param eventMessage The message to be formatted.
     * @param separator    The separator to be used in the format.
     * @return The chat format as a String.
     */
    String getChatFormat(Player player, String eventMessage, String separator);
}