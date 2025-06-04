package dev.revere.alley.game.match.data.impl;

import dev.revere.alley.game.match.data.AbstractMatchData;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public class MatchDataSoloImpl extends AbstractMatchData {
    private final UUID winner;
    private final UUID loser;

    /**
     * Constructor for the MatchDataSoloImpl class.
     *
     * @param winner   The UUID of the winner.
     * @param loser The UUID of the loser.
     */
    public MatchDataSoloImpl(String kit, String arena, UUID winner, UUID loser) {
        super(kit, arena);
        this.loser = loser;
        this.winner = winner;
    }
}