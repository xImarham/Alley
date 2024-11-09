package dev.revere.alley.game.match.player.impl;

import lombok.Getter;
import lombok.Setter;
import dev.revere.alley.game.match.player.GamePlayer;
import dev.revere.alley.game.match.player.data.MatchGamePlayerData;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Setter
@Getter
public class MatchGamePlayerImpl extends GamePlayer {
    private final MatchGamePlayerData data;
    private int elo;

    /**
     * Constructor for the MatchGamePlayerImpl class.
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     * @param elo      The elo of the player.
     */
    public MatchGamePlayerImpl(UUID uuid, String username, int elo) {
        super(uuid, username);
        this.data = new MatchGamePlayerData();
        this.elo = elo;
    }

    /**
     * Constructor for the MatchGamePlayerImpl class.
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     */
    public MatchGamePlayerImpl(UUID uuid, String username) {
        this(uuid, username, 0);
    }
}
