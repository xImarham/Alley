package dev.revere.alley.queue;

import dev.revere.alley.Alley;
import dev.revere.alley.queue.runnable.QueueRunnable;
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
    private final List<Queue> queues = new ArrayList<>();

    public QueueRepository() {
        this.initialize();
    }

    public void initialize() {
        Alley.getInstance().getServer().getScheduler().runTaskTimer(Alley.getInstance(), new QueueRunnable(), 10L, 10L);
    }
}