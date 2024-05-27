package me.emmy.alley.match.player.impl;

import lombok.Getter;
import me.emmy.alley.match.player.GamePlayer;
import me.emmy.alley.match.player.data.MatchGamePlayerData;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
public class MatchGamePlayerImpl extends GamePlayer {

    private final MatchGamePlayerData data;

    /**
     * Constructor for the MatchGamePlayerImpl class.
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     */
    public MatchGamePlayerImpl(UUID uuid, String username) {
        super(uuid, username);
        this.data = new MatchGamePlayerData();
    }
}
