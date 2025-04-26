package dev.revere.alley.feature.leaderboard.data;

import dev.revere.alley.feature.kit.Kit;
import lombok.Data;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @date 3/3/2025
 */
@Data
public class LeaderboardPlayerData {
    private final String name;
    private final UUID uuid;
    private final int elo;
    private Kit kit;

    /**
     * Constructor for the LeaderboardEntry class.
     *
     * @param name The name of the player
     * @param uuid The UUID of the player
     * @param elo  The ELO of the player
     * @param kit  The kit of the player
     */
    public LeaderboardPlayerData(String name, UUID uuid, int elo, Kit kit) {
        this.name = name;
        this.uuid = uuid;
        this.elo = elo;
        this.kit = kit;
    }
}