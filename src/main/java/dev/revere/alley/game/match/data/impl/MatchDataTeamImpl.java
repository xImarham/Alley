package dev.revere.alley.game.match.data.impl;

import dev.revere.alley.game.match.data.AbstractMatchData;
import dev.revere.alley.game.match.snapshot.Snapshot;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

/**
 * @author Emmy
 * @project Alley
 * @since 29/05/2025
 */
@Getter
public class MatchDataTeamImpl extends AbstractMatchData {
    private final List<UUID> playerTeam;
    private final List<UUID> opponentTeam;

    private final List<UUID> winners;

    private final String winnerTeam;

    private final List<Snapshot> snapshots;
    private final List<Snapshot> opponentSnapshots;

    /**
     * Constructor for the MatchDataTeamImpl class.
     *
     * @param playerTeam        The list of player UUIDs in the team.
     * @param opponentTeam      The list of opponent UUIDs in the team.
     * @param winners           The list of winner UUIDs.
     * @param winnerTeam        The name of the winning team.
     * @param snapshots         The list of snapshots for the team.
     * @param opponentSnapshots The list of snapshots for the opponent team.
     */
    public MatchDataTeamImpl(String kit, String arena, List<UUID> playerTeam, List<UUID> opponentTeam, List<UUID> winners, String winnerTeam, List<Snapshot> snapshots, List<Snapshot> opponentSnapshots) {
        super(kit, arena);
        this.playerTeam = playerTeam;
        this.opponentTeam = opponentTeam;
        this.winners = winners;
        this.winnerTeam = winnerTeam;
        this.snapshots = snapshots;
        this.opponentSnapshots = opponentSnapshots;
    }

}