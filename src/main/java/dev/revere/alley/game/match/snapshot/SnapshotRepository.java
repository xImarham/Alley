package dev.revere.alley.game.match.snapshot;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/26/2024
 */
@Getter
@Setter
public class SnapshotRepository {
    private final Map<UUID, Snapshot> snapshots;

    public SnapshotRepository() {
        this.snapshots = new HashMap<>();
    }

    /**
     * Get a snapshot by UUID.
     *
     * @param uuid the UUID of the snapshot
     * @return the snapshot
     */
    public Snapshot getSnapshot(UUID uuid) {
        return this.snapshots.get(uuid);
    }

    /**
     * Get a snapshot by username.
     *
     * @param username the username of the player
     * @return the snapshot, or null if not found
     */
    public Snapshot getSnapshot(String username) {
        return this.snapshots.values().stream()
                .filter(snapshot -> snapshot.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    /**
     * Add a snapshot to the repository.
     *
     * @param snapshot  the snapshot to add
     */
    public void addSnapshot(Snapshot snapshot) {
        this.snapshots.put(snapshot.getUuid(), snapshot);
    }

    /**
     * Remove a snapshot from the repository.
     *
     * @param uuid the UUID of the snapshot to remove
     */
    public void removeSnapshot(UUID uuid) {
        this.snapshots.remove(uuid);
    }
}