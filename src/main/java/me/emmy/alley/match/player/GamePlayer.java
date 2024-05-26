package me.emmy.alley.match.player;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Data
@Getter
@Setter
public class GamePlayer {

    private UUID uuid;
    private String username;
    private boolean disconnected;
    private boolean dead;

    private List<UUID> players;
    private List<UUID> alivePlayers = new ArrayList<>();

    /**
     * Constructor for the GamePlayer class.
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     */
    public GamePlayer(UUID uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    /**
     * Gets the player associated with the GamePlayer.
     *
     * @return The player associated with the GamePlayer.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}