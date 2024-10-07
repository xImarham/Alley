package me.emmy.alley.queue;

import lombok.Getter;
import lombok.Setter;
import me.emmy.alley.Alley;
import me.emmy.alley.queue.runnable.QueueRunnable;

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
        initialize();
    }

    public void initialize() {
        Alley.getInstance().getServer().getScheduler().runTaskTimer(Alley.getInstance(), new QueueRunnable(), 10L, 10L);
    }
}