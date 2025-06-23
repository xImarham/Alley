package dev.revere.alley.base.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.base.kit.setting.impl.mode.KitSettingRankedImpl;
import dev.revere.alley.base.queue.menu.QueuesMenuDefault;
import dev.revere.alley.base.queue.menu.QueuesMenuModern;
import dev.revere.alley.base.queue.runnable.QueueRunnable;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.tool.logger.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class QueueService {
    protected final Alley plugin;
    private final List<Queue> queues;
    private Menu queueMenu;

    /**
     * Constructor for the QueueService class.
     *
     * @param plugin the plugin instance
     */
    public QueueService(Alley plugin) {
        this.plugin = plugin;
        this.queues = new ArrayList<>();
        this.initialize();
        this.queueMenu = this.determineMenu();
    }

    public void initialize() {
        this.plugin.getServer().getScheduler().runTaskTimer(this.plugin, new QueueRunnable(), 10L, 10L);
    }

    /**
     * Determines the menu type based on the configuration.
     *
     * @return the appropriate menu instance
     */
    private Menu determineMenu() {
        FileConfiguration config = this.plugin.getConfigService().getMenusConfig();
        String menuType = config.getString("queues-menu.type");

        switch (menuType) {
            case "MODERN":
                return new QueuesMenuModern();
            case "DEFAULT":
                return new QueuesMenuDefault();
        }

        Logger.log("Invalid menu type specified in config.yml. Defaulting to QueuesMenuDefault.");
        return new QueuesMenuDefault();
    }

    public void reloadQueues() {
        this.queues.clear();
        this.plugin.getKitService().getKits().forEach(kit -> {
            if (!kit.isEnabled()) return;
            new Queue(kit, false, false);
            new Queue(kit, false, true);
            if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
                new Queue(kit, true, false);
            }
        });
    }

    /**
     * Get the player count of a specific game type
     *
     * @param queue the queue to get the player count of
     * @return the player count of the game type
     */
    public int getPlayerCountOfGameType(String queue) {
        EnumProfileState stateForQueue = this.getStateForQueue(queue);

        if (stateForQueue == null) {
            return 0;
        }

        return (int) this.plugin.getProfileService().getProfiles().values().stream()
                .filter(profile -> profile.getState().equals(stateForQueue))
                .filter(profile -> this.isMatchForQueue(profile, queue))
                .count();
    }

    /**
     * Get the state of a profile for a specific queue
     *
     * @param queue the queue to get the state for
     * @return the state of the profile for the queue
     */
    private EnumProfileState getStateForQueue(String queue) {
        switch (queue) {
            case "Unranked":
            case "Ranked":
            case "Duos":
                return EnumProfileState.PLAYING;
            case "FFA":
                return EnumProfileState.FFA;
            case "Bots":
                return EnumProfileState.FIGHTING_BOT;
            default:
                return null;
        }
    }

    /**
     * Check if a profile's match type matches the queue
     *
     * @param profile the profile to check
     * @param queue   the queue to check the profile's match for
     * @return true if the profile's match type matches the queue
     */
    private boolean isMatchForQueue(Profile profile, String queue) {
        if (queue.equals("FFA")) {
            return true;
        } else if (queue.equals("Ranked")) {
            return profile.getMatch().isRanked();
        } else if (queue.equals("Duos")) {
            return true;
        }

        return false;
    }
}