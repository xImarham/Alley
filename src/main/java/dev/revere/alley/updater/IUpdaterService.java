package dev.revere.alley.updater;

import dev.revere.alley.plugin.lifecycle.IService;

import java.io.IOException;

/**
 * @author Remi
 * @project alley-practice
 * @date 24/07/2025
 */
public interface IUpdaterService extends IService {
    void checkForUpdates();

    void downloadAndUpdate(String version);

    String getLatestVersion() throws IOException;
}
