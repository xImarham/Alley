package dev.revere.alley.updater;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.revere.alley.config.IConfigService;
import dev.revere.alley.plugin.AlleyContext;
import dev.revere.alley.plugin.annotation.Service;
import dev.revere.alley.tool.logger.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Remi
 * @project alley-practice
 * @date 24/07/2025
 */
@Service(provides = IUpdaterService.class, priority = 1500)
public class UpdaterService implements IUpdaterService {
    private final IConfigService configService;
    private final String currentVersion;
    private final JavaPlugin plugin;

    private final String githubRepo = "RevereInc/alley-practice";
    private String latestVersion;

    public UpdaterService(IConfigService configService, JavaPlugin plugin) {
        this.configService = configService;
        this.plugin = plugin;
        this.currentVersion = plugin.getDescription().getVersion();
    }

    @Override
    public void setup(AlleyContext context) {
        try {
            this.latestVersion = getLatestVersion();
        } catch (IOException e) {
            Logger.logException("Failed to fetch the latest version from GitHub", e);
        }
    }

    @Override
    public void initialize(AlleyContext context) {
        checkForUpdates();
    }

    @Override
    public void checkForUpdates() {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                    Logger.warn("New version available: " + latestVersion + " (Current: " + currentVersion + ")");

                    if (shouldAutoUpdate()) {
                        downloadAndUpdate(latestVersion);
                    }
                }
            } catch (Exception e) {
                Logger.logException("Failed to check for updates", e);
            }
        });
    }

    @Override
    public void downloadAndUpdate(String version) {
        try {
            String downloadUrl = "https://github.com/" + githubRepo + "/releases/download/v" + version + "/Alley-" + version + ".jar";

            URL url = new URL(downloadUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            File pluginFile = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            File updateFile = new File(pluginFile.getParent(), "Alley-" + version + ".jar");

            try (InputStream inputStream = connection.getInputStream();
                 FileOutputStream outputStream = new FileOutputStream(updateFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            Logger.info("Update downloaded successfully! Restarting plugin to apply the update...");
        } catch (Exception e) {
            Logger.logException("Failed to download and update to version " + version, e);
        }
    }

    @Override
    public String getLatestVersion() throws IOException {
        URL url = new URL("https://api.github.com/repos/" + githubRepo + "/releases/latest");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

        try (InputStream inputStream = connection.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {

            JsonParser parser = new JsonParser();
            JsonObject json = parser.parse(inputStreamReader).getAsJsonObject();
            return json.get("tag_name").getAsString().replace("v", "");
        }
    }

    private boolean shouldAutoUpdate() {
        return configService.getSettingsConfig().getBoolean("auto-update", true);
    }
}
