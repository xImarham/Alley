package me.emmy.alley.queue;

import lombok.Data;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Data
public class QueueProfile {

    private final Queue queue;
    private final UUID uuid;

    private long startTime = System.currentTimeMillis();

    /**
     * Constructor for the QueueProfile class.
     *
     * @param queue The queue.
     * @param uuid  The UUID of the queue.
     */
    public QueueProfile(Queue queue, UUID uuid) {
        this.queue = queue;
        this.uuid = uuid;
    }

    public long getElapsedTime() {
        return System.currentTimeMillis() - startTime;
    }
}
