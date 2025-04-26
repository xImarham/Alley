package dev.revere.alley.feature.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.api.menu.Menu;
import dev.revere.alley.feature.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.feature.queue.menu.QueuesMenuDefault;
import dev.revere.alley.feature.queue.menu.QueuesMenuModern;
import dev.revere.alley.feature.queue.runnable.QueueRunnable;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
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
    private final List<Queue> queues;
    private final Alley plugin;
    private Menu queueMenu;

    /**
     * Constructor for the QueueService class.
     *
     * @param plugin the plugin instance
     */
    public QueueService(Alley plugin) {
        this.queues = new ArrayList<>();
        this.plugin = plugin;
        this.initialize();
        this.determineMenu();
    }

    public void initialize() {
        Alley.getInstance().getServer().getScheduler().runTaskTimer(Alley.getInstance(), new QueueRunnable(), 10L, 10L);
    }

    private void determineMenu() {
        FileConfiguration config = Alley.getInstance().getConfigService().getMenusConfig();
        String menuType = config.getString("queues-menu.type");

        if (menuType.equalsIgnoreCase("MODERN")) {
            this.queueMenu = new QueuesMenuModern();
        } else if (menuType.equalsIgnoreCase("DEFAULT")) {
            this.queueMenu = new QueuesMenuDefault();
        }
    }

    public void reloadQueues() {
        this.queues.clear();
        Alley.getInstance().getKitService().getKits().forEach(kit -> {
            if (!kit.isEnabled()) return;
            new Queue(kit, false);

            if (kit.isSettingEnabled(KitSettingRankedImpl.class)) {
                new Queue(kit, true);
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
        }

        return false;
    }
}