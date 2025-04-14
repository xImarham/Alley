package dev.revere.alley.api.discord.impl;

import dev.revere.alley.api.discord.DiscordBridge;
import dev.revere.alley.api.discord.interfaces.IDiscordWebhookService;
import dev.revere.alley.api.discord.util.Logger;
import dev.revere.alley.util.chat.CC;
import lombok.Getter;
import lombok.var;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * @author Emmy
 * @project DiscordBridge
 * @since 12/04/2025
 */
@Getter
public class DiscordWebhookServiceImpl implements IDiscordWebhookService {

    /**
     * Sends a simple message to the Discord webhook URL.
     *
     * @param message The message to send.
     */
    @Override
    public void sendMessage(String message) {
        this.sendPayload(message);
    }

    /**
     * Sends an embed message to the Discord webhook URL.
     *
     * @param title       The title of the embed.
     * @param description The description of the embed.
     * @param color       The color of the embed in hex format (e.g., "FF0000" for red).
     * @param footer      The footer text of the embed.
     */
    @Override
    public void sendEmbed(String title, String description, String color, String footer) {
        this.sendEmbed(title, description, color, footer, null);
    }

    /**
     * Sends an embed message to the Discord webhook URL.
     *
     * @param title       The title of the embed.
     * @param description The description of the embed.
     * @param color       The color of the embed in hex format (e.g., "FF0000" for red).
     * @param footer      The footer text of the embed.
     * @param imageUrl    The URL of the image to include in the embed (optional).
     */
    @Override
    @SuppressWarnings("unchecked")
    public void sendEmbed(String title, String description, String color, String footer, String imageUrl) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscordBridge.getInstance().getPlugin(), () -> {
            try {
                if (title == null || description == null || color == null || footer == null) {
                    Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&cDiscordBridge&7] &cMissing required embed fields: title, description, color, and footer must all be provided."));
                    return;
                }

                JSONObject embed = new JSONObject();
                embed.put("title", title);
                embed.put("description", description);
                embed.put("color", Integer.parseInt(color, 16));

                JSONObject footerObj = new JSONObject();
                footerObj.put("text", footer);
                embed.put("footer", footerObj);

                if (imageUrl != null) {
                    JSONObject image = new JSONObject();
                    image.put("url", imageUrl);
                    embed.put("image", image);
                }

                JSONObject payload = new JSONObject();
                payload.put("embeds", Collections.singletonList(embed));

                this.sendJson(payload.toJSONString());

            } catch (NumberFormatException exception) {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&cDiscordBridge&7] &cInvalid color format. Ensure the color is a valid hex code (e.g., 'FF0000')."));
            } catch (Exception exception) {
                Logger.logException("Failed to send embed to Discord", exception);
            }
        });
    }

    /**
     * Sends a simple message payload to the Discord webhook URL.
     *
     * @param value The message to send.
     */
    @SuppressWarnings("unchecked")
    private void sendPayload(String value) {
        if (value == null || value.isEmpty()) {
            Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&cDiscordBridge&7] &cMessage content is empty. Please provide a valid message."));
            return;
        }

        JSONObject json = new JSONObject();
        json.put("content", value);
        this.sendJson(json.toJSONString());
    }

    /**
     * Sends a JSON payload to the Discord webhook URL.
     *
     * @param payload The JSON payload to send.
     */
    private void sendJson(String payload) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(DiscordBridge.getInstance().getWebhookUrl()).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; DiscordBot/1.0)");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = connection.getResponseCode();
            if (responseCode >= 400) {
                String errorMessage = getErrorMessage(connection);
                Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&cDiscordBridge&7] &cFailed to send message to Discord. HTTP error code: " + responseCode + ". Error: " + errorMessage));
            } else {
                Bukkit.getConsoleSender().sendMessage(CC.translate("&7[&cDiscordBridge&7] &aMessage sent to Discord successfully!"));
                connection.getInputStream().close();
            }
        } catch (Exception exception) {
            Logger.logException("Failed to send message to Discord", exception);
        }
    }

    /**
     * Helper method to extract error message from the connection's error stream.
     *
     * @param connection The HTTP connection.
     * @return The error message.
     * @throws Exception if there's an issue reading the error stream.
     */
    private String getErrorMessage(HttpURLConnection connection) throws Exception {
        try (var is = connection.getErrorStream()) {
            StringBuilder errorBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    errorBuilder.append(line);
                }
            }
            return errorBuilder.toString();
        }
    }
}