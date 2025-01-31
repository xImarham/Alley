package dev.revere.alley.feature.queue;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.Alley;
import dev.revere.alley.feature.hotbar.enums.HotbarType;
import dev.revere.alley.feature.kit.Kit;
import dev.revere.alley.profile.Profile;
import dev.revere.alley.profile.enums.EnumProfileState;
import dev.revere.alley.util.chat.CC;
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
    private final boolean ranked;
    private final Kit kit;

    /**
     * Constructs a new Queue with the specified kit and adds it to the queue repository.
     *
     * @param kit The kit associated with the queue.
     */
    public Queue(Kit kit, boolean ranked) {
        this.kit = kit;
        this.ranked = ranked;
        Alley.getInstance().getQueueRepository().getQueues().add(this);
    }

    /**
     * Gets the amount of people playing that queue.
     *
     * @return The amount of people playing that queue.
     */
    public int getQueueFightCount() {
        return Alley.getInstance().getMatchRepository().getMatches().stream().filter(match -> match.getQueue().equals(this)).toArray().length;
    }

    /**
     * Gets the queue type.
     *
     * @return The queue type.
     */
    public String getQueueType() {
        return (this.ranked ? "Ranked" : "Unranked");
    }

    /**
     * Adds a player to the queue.
     *
     * @param player The player to add.
     */
    public void addPlayer(Player player, int elo) {
        UUID uuid = player.getUniqueId();

        if (this.profiles.stream().anyMatch(queueProfile -> queueProfile.getUuid().equals(uuid))) {
            player.sendMessage(CC.translate("&cYou're already in a queue."));
            return;
        }

        QueueProfile queueProfile = new QueueProfile(this, uuid);
        queueProfile.setElo(elo);

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(uuid);
        profile.setQueueProfile(queueProfile);
        profile.setState(EnumProfileState.WAITING);

        this.profiles.add(queueProfile);

        player.sendMessage(CC.translate("&aYou've joined the &b" + queueProfile.getQueue().getKit().getName() + " &aqueue."));
    }

    /**
     * Removes a player from the queue.
     *
     * @param queueProfile The queue profile to remove.
     */
    public void removePlayer(QueueProfile queueProfile) {
        this.profiles.remove(queueProfile);

        Profile profile = Alley.getInstance().getProfileRepository().getProfile(queueProfile.getUuid());
        profile.setQueueProfile(null);
        profile.setState(EnumProfileState.LOBBY);

        Player player = Bukkit.getPlayer(queueProfile.getUuid());
        if (player == null) {
            return;
        }

        Alley.getInstance().getHotbarRepository().applyHotbarItems(player, HotbarType.LOBBY);
        player.sendMessage(CC.translate("&cYou've left the queue."));
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