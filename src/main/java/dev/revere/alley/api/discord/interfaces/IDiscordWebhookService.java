package dev.revere.alley.api.discord.interfaces;

/**
 * @author Emmy
 * @project DiscordBridge
 * @since 12/04/2025
 */
public interface IDiscordWebhookService {
    /**
     * Sends a message to the Discord webhook.
     *
     * @param message The message to send.
     */
    void sendMessage(String message);

    /**
     * Sends an embed message to the Discord webhook without an image.
     *
     * @param title       The title of the embed.
     * @param description The description of the embed.
     * @param color       The color of the embed.
     * @param footer      The footer of the embed.
     */
    void sendEmbed(String title, String description, String color, String footer);

    /**
     * Sends an embed message with an image to the Discord webhook with the specified image URL.
     *
     * @param title       The title of the embed.
     * @param description The description of the embed.
     * @param color       The color of the embed.
     * @param footer      The footer of the embed.
     * @param imageUrl    The URL of the image to include in the embed.
     */
    void sendEmbed(String title, String description, String color, String footer, String imageUrl);
}