package me.emmy.alley.match.player.impl;

import me.emmy.alley.match.player.GamePlayer;

import java.util.UUID;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
public class MatchGamePlayerImpl extends GamePlayer {

    /**
     * Constructor for the MatchGamePlayerImpl class.
     *
     * @param uuid     The UUID of the player.
     * @param username The username of the player.
     */
    public MatchGamePlayerImpl(UUID uuid, String username) {
        super(uuid, username);
    }
}
