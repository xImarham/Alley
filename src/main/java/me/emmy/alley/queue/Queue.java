package me.emmy.alley.queue;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.kit.Kit;
import me.emmy.alley.profile.Profile;
import me.emmy.alley.profile.enums.EnumProfileState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class Queue {

    private final LinkedList<QueueProfile> profiles = new LinkedList<>();
    private final UUID uuid = UUID.randomUUID();
    private final Kit kit;

    /**
     * Constructs a new Queue with the specified kit and adds it to the queue repository.
     *
     * @param kit The kit associated with the queue.
     */
    public Queue(Kit kit) {
        this.kit = kit;
        Alley.getInstance().getQueueRepository().getQueues().add(this);
    }

    /**
     * Adds a player to the queue.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        QueueProfile queueProfile = new QueueProfile(this, uuid);
        Profile profile = Alley.getInstance().getProfileRepository().getProfile(uuid);

        profile.setQueueProfile(queueProfile);
        profile.setState(EnumProfileState.WAITING);

        Alley.getInstance().getHotbarUtility().applySpawnItems(player);

        profiles.add(queueProfile);
    }

    /**
     * Removes a player from the queue.
     *
     * @param queueProfile The queue profile to remove.
     */
    public void removePlayer(QueueProfile queueProfile) {
        profiles.remove(queueProfile);

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(queueProfile.getUuid());
        profile.setQueueProfile(null);
        profile.setState(EnumProfileState.LOBBY);

        Player player = Bukkit.getPlayer(queueProfile.getUuid());

        if (player == null) {
            return;
        }

        Alley.getInstance().getHotbarUtility().applySpawnItems(player);
        player.sendMessage("You have left the queue.");
    }

    /**
     * Gets the profile of a player.
     *
     * @param uuid The UUID of the player.
     * @return The profile object.
     */
    public Profile getProfile(UUID uuid) {
        return Alley.getInstance().getProfileRepository().getProfile(uuid);
    }
}
