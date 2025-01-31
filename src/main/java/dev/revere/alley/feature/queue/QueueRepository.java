package dev.revere.alley.feature.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.feature.kit.settings.impl.KitSettingRankedImpl;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.feature.queue.runnable.QueueRunnable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class QueueRepository {
    private final List<Queue> queues;
    private final Alley plugin;

    /**
     * Constructor for the QueueRepository class.
     *
     * @param plugin the plugin instance
     */
    public QueueRepository(Alley plugin) {
        this.queues = new ArrayList<>();
        this.plugin = plugin;
        this.initialize();
    }

    public void initialize() {
        Alley.getInstance().getServer().getScheduler().runTaskTimer(Alley.getInstance(), new QueueRunnable(), 10L, 10L);
    }

    public void reloadQueues() {
        this.queues.clear();
        Alley.getInstance().getKitRepository().getKits().forEach(kit -> {
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
        EnumProfileState stateForQueue = getStateForQueue(queue);

        if (stateForQueue == null) {
            return 0;
        }

        return (int) this.plugin.getProfileRepository().getProfiles().values().stream()
                .filter(profile -> profile.getState().equals(stateForQueue))
                .filter(profile -> isMatchForQueue(profile, queue))
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
            default:
                return null;
        }
    }

    /**
     * Check if a profile's match type matches the queue
     *
     * @param profile the profile to check
     * @param queue the queue to check the profile's match for
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