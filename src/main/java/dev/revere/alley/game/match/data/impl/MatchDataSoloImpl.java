package dev.revere.alley.game.match.data.impl;

import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.game.match.snapshot.Snapshot;
import lombok.Getter;

import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public class MatchDataSoloImpl extends AbstractMatchData {
    private final UUID player;
    private final UUID opponent;

    private final UUID winner;

    private final Snapshot snapshot;
    private final Snapshot opponentSnapshot;

    /**
     * Constructor for the MatchDataSoloImpl class.
     *
     * @param player           The UUID of the player.
     * @param opponent         The UUID of the opponent.
     * @param winner           The UUID of the winner.
     * @param snapshot         The snapshot of the player.
     * @param opponentSnapshot The snapshot of the opponent.
     */
    public MatchDataSoloImpl(String kit, String arena, UUID winner, UUID player, UUID opponent, Snapshot snapshot, Snapshot opponentSnapshot) {
        super(kit, arena);
        this.player = player;
        this.opponent = opponent;
        this.winner = winner;
        this.snapshot = snapshot;
        this.opponentSnapshot = opponentSnapshot;
    }
}