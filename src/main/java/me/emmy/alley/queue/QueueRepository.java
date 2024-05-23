package me.emmy.alley.queue;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Setter
public class QueueRepository {
    private final List<Queue> queues = new ArrayList<>();

    /**
     * Method to get a queue by its UUID.
     *
     * @param uuid The UUID of the queue.
     * @return The queue.
     */
    public Queue getQueueByUUID(UUID uuid) {
        return queues.stream().filter(queue -> queue.getUuid().toString().equals(uuid)).findFirst().orElse(null);
    }
}
